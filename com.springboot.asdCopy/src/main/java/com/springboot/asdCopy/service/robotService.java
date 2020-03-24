package com.springboot.asdCopy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.springboot.asdCopy.entity.Robot;


public interface robotService {
	public List<Robot> getAllInfo();

	public int updateButtonStatus(@Param("name") String name, @Param("status") String status);

	public String getButtonStatus(@Param("name") String name);
	
	public int updateCoordsByMqtt(@Param("mqttMap") Map<String, String> map);
}
