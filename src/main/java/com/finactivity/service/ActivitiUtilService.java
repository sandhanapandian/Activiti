package com.finactivity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.finactivity.data.TaskDto;
import com.finactivity.data.TaskResponseDto;
import com.finactivity.data.WorkFlowSearchDto;
import com.finactivity.model.ActUser;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ActivitiUtilService {

	private static final String ENROLLMENT_PROCESS_KEY = "enrollmentProcess";

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public ProcessInstance startBPMNProcess(Map<String, Object> vars) {
		try {
			return runtimeService.startProcessInstanceByKey(ENROLLMENT_PROCESS_KEY, vars);
		} catch (Exception e) {
			log.error("Exception while starting activity", e);
			return null;
		}
	}


	public static ActUser getCurrentUser() {

		if(SecurityContextHolder.getContext().getAuthentication()==null){
			return null;
		}
		
		Object authObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(authObject == null || authObject.equals("anonymousUser")) {
			return null;
		} else {
			return (ActUser) authObject;
		}
	}

	/**
	 * Checks User, If not exists create new User and Map Group Membership
	 * @param String
	 * @return void
	 */
	public void createUser(ActUser userProfile){
		User user = identityService.createUserQuery().userId(userProfile.getId().toString()).singleResult();
		
		if(user == null || user.getId() == null) {
			//Create User in ACT_ID_USER Table
			user = identityService.newUser(userProfile.getId().toString());
			user.setFirstName(userProfile.getFirstName());
			user.setLastName(userProfile.getLastName());
			user.setEmail(userProfile.getEmail());
			user.setPassword(userProfile.getPassword());
			identityService.saveUser(user);
			String[] workFlowGroupList = userProfile.getGroups().split(","); 
			for(String wfg : workFlowGroupList) {
				createGroup(wfg);
				identityService.createMembership(userProfile.getId().toString(), wfg);
			}
		}
	}

	public void createGroup(String groupName){
		/** Group Name Trim and Check */
		Group group = identityService.createGroupQuery().groupId(groupName.trim().toString()).singleResult();
		if(group == null || group.getId() == null) {
		  group = identityService.newGroup(groupName.toString());
		  group.setName(groupName.toString());
		  group.setType("Active");
		  log.info("Create Group :: "+groupName);
		  identityService.saveGroup(group);
		  log.info("Group Created");
		}
	}

	/**
	 * Checks Group, If not exists create new group
	 * @param String
	 * @return String
	 */
	public String checkAndCreateGroupName(String groupName) {
		try {
			
			createGroup(groupName);
			ActUser userProfile = getCurrentUser();
			if(userProfile != null && userProfile.getId() != null) {
				boolean mapGroup = false;
				String[] workFlowGroupList = userProfile.getGroups().split(","); 
				if(workFlowGroupList != null && workFlowGroupList.length > 0) {
					for(String wfg : workFlowGroupList) {
						if(wfg.equals(groupName)) {
							mapGroup = true;
						}
					}
				}
				if(mapGroup) {
					createUser(userProfile);
					List<Group> groupList = identityService.createGroupQuery().groupMember(userProfile.getId().toString()).list();
					boolean alreadyMappedInGroup = false;
					for(Group localGroup : groupList) {
						if(localGroup.getName().equals(groupName)) {
							alreadyMappedInGroup = true;
						}
					}
					if(!alreadyMappedInGroup) {
						identityService.createMembership(userProfile.getId().toString(), groupName.toString());
					}
				}
			}
			return groupName;
		} catch (Exception e) {
			log.error("Error in group name checking", e);
		}
		return null;
	}
	 

	/**
	 * Checks the specified task with task id is claimed or not
	 * @param String
	 * @return boolean
	 */
	public boolean isClaimed(String taskId) {
		String sql = "select ASSIGNEE_ from ACT_RU_TASK where ID_=?";
		String assignee = jdbcTemplate.queryForObject(sql, String.class,
				new Object[] { taskId });
		if (assignee == null || assignee.equalsIgnoreCase("null")) {
			return false;
		}
		return true;
	}
	
	public boolean claimTask(String taskId, String userId) {
		try{
			taskService.claim(taskId, userId);
			return true;
		} catch(Exception ex){
			return false;
		}
	}
	
    public TaskResponseDto getTasks(WorkFlowSearchDto data) {
    	TaskResponseDto taskResponse = new TaskResponseDto();
    	log.info("data.getGroupList() -- "+ data.getGroupList()+ " || assignee - "+data.getAssignee());
		List<Task> ownedTaskList = taskService.createTaskQuery().taskAssignee(data.getAssignee()).includeProcessVariables().includeTaskLocalVariables().list();
    	log.info("ownedTaskList -- "+ownedTaskList);
		List<Task> taskListTemp = taskService.createTaskQuery().taskCandidateGroupIn(data.getGroupList()).taskUnassigned().includeProcessVariables().includeTaskLocalVariables().list();
		log.info("TaskList -"+taskListTemp);
		List<TaskDto> responseList = new ArrayList<TaskDto>();
		List<Task> taskList = new ArrayList<Task>();
		if(ownedTaskList != null && !ownedTaskList.isEmpty()) {
			taskList.addAll(ownedTaskList);
		}
		if(taskListTemp != null && !taskListTemp.isEmpty()) {
			taskList.addAll(taskListTemp);
		}
		int totalRecords = taskList.size();
		taskResponse.setTotalRecords(totalRecords);
		int lastindex =((data.getPage() * data.getSize())+data.getSize());
		if(totalRecords < lastindex) {
			lastindex = totalRecords;
		}
		taskList = taskList.subList((data.getPage() * data.getSize()), lastindex);
		TaskDto response = null;
		for(Task task : taskList ) {
			response = new TaskDto();
			response.setTaskId(task.getId());
			response.setClaimStatus(task.getAssignee() == null ? false : true);
			response.setCreationTime(task.getCreateTime());
			response.setProcessInstanceId(task.getProcessInstanceId());
			response.setTaskName(task.getName());

			if(task.getTaskLocalVariables() != null && task.getTaskLocalVariables().keySet() != null && !task.getTaskLocalVariables().keySet().isEmpty()) {
				for(String key : task.getTaskLocalVariables().keySet()) {
					log.info("Key : "+key+" || Value :: "+task.getTaskLocalVariables().get(key));
					if("groupName".equals(key)) {
						response.setCategory(task.getTaskLocalVariables().get(key).toString());
					}
				}
			}
			responseList.add(response);
			log.info("<< --------END--------- >> ");
		}
		taskResponse.setTaskList(responseList);
		return taskResponse;
    }


    public void completeTask(String taskId, String taskTO){

		Map<String, Object> variables = new HashMap<String, Object>();
		
		taskService.complete(taskId, variables);
    }

}
