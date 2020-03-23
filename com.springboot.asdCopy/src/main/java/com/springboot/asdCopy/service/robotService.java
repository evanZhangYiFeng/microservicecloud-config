package com.springboot.asdCopy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.springboot.asdCopy.entity.Robot;


public interface robotService {
	public List<Robot> getAllInfo();

	public int updateButtonStatus(@Param("name") String name, @Param("status") String status);

	public String getButtonStatus(@Param("name") String name);
}
