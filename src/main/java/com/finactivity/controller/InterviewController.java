package com.finactivity.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finactivity.data.TaskResponseDto;
import com.finactivity.data.WorkFlowSearchDto;
import com.finactivity.model.Interview;
import com.finactivity.service.ActivitiUtilService;
import com.finactivity.service.InterviewService;

@RestController
@RequestMapping(value = "/api/v1/interview")
public class InterviewController {

	@Autowired
	private InterviewService interviewService;

	@Autowired
	private  ActivitiUtilService activitiUtilService;

	@RequestMapping(value = "/invite", method = RequestMethod.POST)
	public Object inviteCandidate(@RequestBody Interview requestData, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		
		return interviewService.inviteCandidate(requestData, httpRequest, httpResponse);
	}

	@RequestMapping(value = "/accept/{id}", method = RequestMethod.GET)
	public Object candidateAccepted(@PathVariable("id") Long id, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		
		return interviewService.candidateAccept(id, httpRequest, httpResponse);
	}
	@RequestMapping(value = "/declined/{id}", method = RequestMethod.GET)
	public Object candidateRejected(@PathVariable("id") Long id, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		
		return interviewService.candidateRejected(id, httpRequest, httpResponse);
	}

	@RequestMapping(value = "/first/{id}/{mark}", method = RequestMethod.GET)
	public Object processFirstRound(@PathVariable("id") Long id, @PathVariable("mark") Integer mark, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		
		return interviewService.processFirstRound(id, mark, httpRequest, httpResponse);
	}
	@RequestMapping(value = "/second/{id}/{mark}", method = RequestMethod.GET)
	public Object processSecondRound(@PathVariable("id") Long id, @PathVariable("mark") Integer mark, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		
		return interviewService.processSecondRound(id, mark, httpRequest, httpResponse);
	}

	@RequestMapping(value = "/complete/{taskId}/{taskName}", method = RequestMethod.GET)
	public boolean completeTask(@PathVariable("taskId") String taskId, @PathVariable("taskName") String taskName, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		
		return interviewService.completeTask(taskId, taskName);
	}

	@RequestMapping(value = "/claim/{taskId}/{userId}", method = RequestMethod.GET)
	public boolean claimTask(@PathVariable("taskId") String taskId, @PathVariable("userId") String userId, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		
		return activitiUtilService.claimTask(taskId, userId);
	}

	

	@RequestMapping(value = "/tasks", method = RequestMethod.POST)
    public TaskResponseDto getTasks(@RequestBody WorkFlowSearchDto data) {
		return activitiUtilService.getTasks(data);
    }
}