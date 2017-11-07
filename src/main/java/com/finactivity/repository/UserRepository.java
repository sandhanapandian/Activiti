package com.finactivity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finactivity.model.ActUser;

public interface UserRepository extends JpaRepository<ActUser, Long>{
	public ActUser findByEmail(String userName);
	
	public ActUser findById(Long userId);
}
