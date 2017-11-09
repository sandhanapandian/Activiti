package com.finactivity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finactivity.model.Interview;

public interface INterviewRepository extends JpaRepository<Interview, Long>{
	public Interview findByEmail(String userName);
	
	public Interview findById(Long userId);
}
