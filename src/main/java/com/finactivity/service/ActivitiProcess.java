package com.finactivity.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class ActivitiProcess  implements TaskListener, JavaDelegate{
	
	private static final long serialVersionUID = 1L;
	
	public void processUpdate(){
		log.info("<---------Task Completed------->");
	}
	
	@Override
	public void notify(DelegateTask task) {
		log.info("<--Starts TicketProcess .notify-->");
		try{
			DelegateExecution execution = task.getExecution();		
			execution.setVariable("notes", task.getVariable("notes"));
			execution.setVariable("groupName", task.getVariable("groupName"));
			if(task.getVariable("closurePriority") != null)
				execution.setVariable("closurePriority", task.getVariable("closurePriority"));
			if(task.getVariable("stakeHolder") != null)
				execution.setVariable("stakeHolder", task.getVariable("stakeHolder"));
			log.info("<---Notify Method---->");
		}
		catch(Exception ex){
			log.error("Ticket Process notify method ", ex);
		}
		log.info("<--Ends TicketProcess .notify-->");
	}

	@Override
	public void execute(DelegateExecution execution) {
		log.info("<-----------Task Assigned to User ----->");
		
	}

}
