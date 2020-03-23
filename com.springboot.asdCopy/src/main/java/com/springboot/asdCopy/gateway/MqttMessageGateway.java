package com.springboot.asdCopy.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.integration.mqtt.support.MqttHeaders;

/**
 * Title : MqttMessageGateway.java
 * Description : 消息发送接口，不需要实现，spring会通过代理的方式实现
 * Create on : 2018/1/4 10:58
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttMessageGateway {

	void sendToMqtt(String data,@Header(MqttHeaders.TOPIC) String topic);
}
