package com.finactivity.data;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 6653832502102424516L;
	
	private String sassId;
	private String userName;
	private String password;
}