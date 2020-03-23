package com.springboot.asdCopy.common;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

/**
 * 前端点击按钮，将按钮当前状态发送至mqtt代理服务器
 * @author Administrator
 *
 */
@Component
public class MqttProducerConfig {

	@Value("${spring.mqtt.username}")
	private String username;

	@Value("${spring.mqtt.password}")
	private String password;

	@Value("${spring.mqtt.url}")
	private String hostUrl;

	@Value("${spring.mqtt.clientid}")
	private String clientId;

	private static final String defaultTopic = "send";

	@Bean
	public MessageChannel getProducerChannel() {
		return new DirectChannel();
	}

	// 将MqttConnectOptions注入到容器中
	@Bean
	public MqttConnectOptions getMqttConnectOptions() {
		MqttConnectOptions mOptions = new MqttConnectOptions();
		mOptions.setUserName(username);
		mOptions.setPassword(password.toCharArray());
		mOptions.setServerURIs(new String[] { hostUrl });
		mOptions.setKeepAliveInterval(1);
		return mOptions;
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(getMqttConnectOptions());
		return factory;
	}
	
	//通道适配器
	@Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(defaultTopic);
        return messageHandler;
    }
}
