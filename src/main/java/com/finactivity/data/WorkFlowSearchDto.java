package com.finactivity.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class WorkFlowSearchDto implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private List<String> groupList = new ArrayList<String>();
	
	@Getter @Setter
	private String assignee;
	
	@Getter @Setter
	private int page;
	
	@Getter @Setter
	private int size;
	
}
