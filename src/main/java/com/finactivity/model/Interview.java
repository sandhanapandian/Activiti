package com.finactivity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "interview", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "email" }, name = "UK_INTERVIEW_EMAIL")}

)
@Data
public class Interview {
	

	@Id
	@GeneratedValue
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private Date dateOfBirth;

	private String email;

	private String address;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String pin;

	private String designation;

	private String taskId;
	
	private String processInstanceId;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private InterviewStatus interviewStatus = InterviewStatus.Invited;
	
//	min 40 out 0f 100
	private Integer firstRoundMarks;
	
//	min 40 out 0f 100
	private Integer secondRoundMarks;
	
	
}
