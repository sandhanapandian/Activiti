package com.finactivity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finactivity.model.Interview;
import com.finactivity.model.InterviewStatus;
import com.finactivity.repository.INterviewRepository;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class InterviewService {

	@Autowired
	INterviewRepository interviewRepository;

	@Autowired
	ActivitiUtilService activitiUtilService;

	@Autowired
	TaskService taskService;

	@Transactional
	public Object inviteCandidate(Interview requestData, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {

		requestData = interviewRepository.save(requestData);

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("candidateId", requestData.getId());
		vars.put("group", "Invite Sent");
		vars.put("candidateGroup", "Invite Candidate");

		ProcessInstance processInstance = activitiUtilService.startInterviewBPMNProcess(vars);
		if (processInstance != null)
			requestData.setProcessInstanceId(processInstance.getId());

		TaskQuery activitiQuery = taskService.createTaskQuery();
		activitiQuery.processInstanceId(requestData.getProcessInstanceId());
		activitiQuery.orderByTaskCreateTime().desc();
		List<Task> taskList = activitiQuery.list();

		for (Task task : taskList) {
			requestData.setTaskId(task.getId());
		}

		requestData = interviewRepository.save(requestData);

		taskService.setVariablesLocal(requestData.getTaskId(), vars);
		return requestData;
	}

	@Transactional
	public Object candidateAccept(Long candidateId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		Interview entity = interviewRepository.findById(candidateId);
		entity.setInterviewStatus(InterviewStatus.Interview_accepted);
		entity = interviewRepository.save(entity);

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("candidateId", entity.getId());
		vars.put("group", "Invite Accepted");
		vars.put("inviteAccepted", true);

		taskService.complete(entity.getTaskId(), vars);
		TaskQuery activitiQuery = taskService.createTaskQuery();
		activitiQuery.processInstanceId(entity.getProcessInstanceId());
		activitiQuery.orderByTaskCreateTime().desc();
		List<Task> taskList = activitiQuery.list();

		for (Task task : taskList) {
			entity.setTaskId(task.getId());
		}

		taskService.setVariablesLocal(entity.getTaskId(), vars);
		entity = interviewRepository.save(entity);
		return null;
	}

	@Transactional
	public Object candidateRejected(Long candidateId, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {

		Interview entity = interviewRepository.findById(candidateId);
		entity.setInterviewStatus(InterviewStatus.Interview_Declined);
		entity = interviewRepository.save(entity);

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("candidateId", entity.getId());
		vars.put("group", "Invite Declined");
		vars.put("inviteAccepted", false);

		taskService.complete(entity.getTaskId(), vars);

		TaskQuery activitiQuery = taskService.createTaskQuery();
		activitiQuery.processInstanceId(entity.getProcessInstanceId());
		activitiQuery.orderByTaskCreateTime().desc();
		List<Task> taskList = activitiQuery.list();

		for (Task task : taskList) {
			entity.setTaskId(task.getId());
		}
		taskService.setVariablesLocal(entity.getTaskId(), vars);

		entity = interviewRepository.save(entity);
		return null;
	}

	@Transactional
	public Object processFirstRound(Long candidateId, Integer mark, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		Interview entity = interviewRepository.findById(candidateId);
		entity.setFirstRoundMarks(mark);
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("candidateId", entity.getId());
		if (mark >= 40) {
			entity.setInterviewStatus(InterviewStatus.Selected_FirstRound);
			vars.put("group", "First Round Selected");
			vars.put("candidateGroup", "FirstRound");
			vars.put("firstRoundSelected", true);
		} else {
			entity.setInterviewStatus(InterviewStatus.Rejected_FirstRound);
			vars.put("group", "First Round Rejected");
			vars.put("firstRoundSelected", false);
		}
		entity = interviewRepository.save(entity);

		taskService.complete(entity.getTaskId(), vars);

		TaskQuery activitiQuery = taskService.createTaskQuery();
		activitiQuery.processInstanceId(entity.getProcessInstanceId());
		activitiQuery.orderByTaskCreateTime().desc();
		List<Task> taskList = activitiQuery.list();

		for (Task task : taskList) {
			entity.setTaskId(task.getId());
		}

		taskService.setVariablesLocal(entity.getTaskId(), vars);
		entity = interviewRepository.save(entity);
		return null;
	}

	@Transactional
	public Object processSecondRound(Long candidateId, Integer mark, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		Interview entity = interviewRepository.findById(candidateId);
		entity.setFirstRoundMarks(mark);
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("candidateId", entity.getId());
		if (mark >= 40) {
			entity.setInterviewStatus(InterviewStatus.Selected_FirstRound);
			vars.put("group", "First Round Selected");
			vars.put("candidateGroup", "SecondRound");
			vars.put("firstRoundSelected", true);
		} else {
			entity.setInterviewStatus(InterviewStatus.Rejected_FirstRound);
			vars.put("group", "First Round Rejected");
			vars.put("firstRoundSelected", false);
		}
		entity = interviewRepository.save(entity);

		taskService.complete(entity.getTaskId(), vars);

		TaskQuery activitiQuery = taskService.createTaskQuery();
		activitiQuery.processInstanceId(entity.getProcessInstanceId());
		activitiQuery.orderByTaskCreateTime().desc();
		List<Task> taskList = activitiQuery.list();

		for (Task task : taskList) {
			entity.setTaskId(task.getId());
		}

		taskService.setVariablesLocal(entity.getTaskId(), vars);
		entity = interviewRepository.save(entity);
		return null;
	}

	public boolean completeTask(String taskId, String taskName) {

		Map<String, Object> vars = new HashMap<String, Object>();
		if (taskName.equals("parallelTask1")) {

			vars.put("candidateGroup", "parallelTask1");
		} else if (taskName.equals("parallelTask2")) {

			vars.put("candidateGroup", "parallelTask2");
		} else if (taskName.equals("Final Task")) {

			vars.put("candidateGroup", "Final Task");
		}

		taskService.complete(taskId, vars);

		return true;
	}

}