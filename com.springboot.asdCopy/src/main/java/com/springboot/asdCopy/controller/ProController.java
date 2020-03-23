package com.springboot.asdCopy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.asdCopy.gateway.MqttMessageGateway;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class ProController {
	@Autowired
	MqttMessageGateway gateway;
	
	@CrossOrigin
	@RequestMapping("/proSend")
	public String postMessage(@RequestBody String data) {
		System.out.println(data);
		JSONArray arr = JSONArray.fromObject(data);
		JSONObject object = arr.getJSONObject(0);
		String message = object.get("data").toString();
		String topic = object.get("topic").toString();
		gateway.sendToMqtt(message, topic);
		return "success";
	}
}
