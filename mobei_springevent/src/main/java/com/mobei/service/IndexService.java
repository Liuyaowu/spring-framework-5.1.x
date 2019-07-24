package com.mobei.service;

import com.mobei.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class IndexService {

	@Autowired
	Dao indexDao1Impl;

	@PostConstruct
	public void init(){
		System.out.println(indexDao1Impl.getClass().getName());
	}

}
