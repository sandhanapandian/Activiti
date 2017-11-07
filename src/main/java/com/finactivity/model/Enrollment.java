package com.finactivity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "enrollment")
@Data
public class Enrollment {
	

	@Id
	@GeneratedValue
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private Date dateOfBirth;

	private String course;

	private String address;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String pin;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private AdmissionStatus admissionStatus = AdmissionStatus.Pending;
}
