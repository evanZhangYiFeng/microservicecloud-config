package com.springboot.asdCopy.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.asdCopy.common.MqttReceiveConfig;
import com.springboot.asdCopy.entity.Robot;
import com.springboot.asdCopy.mapper.robotMapper;
import com.springboot.asdCopy.service.robotService;


@Service
public class robotServiceImp implements robotService {

	@Autowired
	private robotMapper mapper;

	@Autowired
	private MqttReceiveConfig config;

	public List<Robot> getAllInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public int updateButtonStatus(String name, String status) {
		mapper.updateButtonStatus(name, status);
		System.out.println(name+","+status);
		return 0;
	}

	public String getButtonStatus(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateCoordsByMqtt(Map<String, String> map) {
		return mapper.updateCoordsByMqtt(map);
	}

}
