package com.finactivity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "act_user")
@Data
public class ActUser {
	

	@Id
	@GeneratedValue
	private Long id;
	
	private String firstName;

	private String lastName;
	
	private String password;
	
	private String groups;
	
	private Date dateOfBirth;

	private String email;
	
	private Date createdDate = new Date();
	
	private Date modifiedDate = new Date();
	
}
