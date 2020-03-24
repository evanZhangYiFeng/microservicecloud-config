package com.springboot.asdCopy.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.springboot.asdCopy.service.robotService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 从mqtt代理服务器读取数据并存到数据库
 * @author Administrator
 *
 */
@Component
public class MqttReceiveConfig {

	@Value("${spring.mqtt.username}")
	private String username;

	@Value("${spring.mqtt.password}")
	private String password;

	@Value("${spring.mqtt.url}")
	private String hostUrl;

	@Value("${spring.mqtt.clientid}")
	private String clientId;

//	@Value("${spring.mqtt.topic}")
	private static final String defaultTopic = "goods";

	@Value("${spring.mqtt.timeout}")
	private int completionTimeout; // 连接超时

	@Value("${spring.mqtt.keepalive}")
	private int keepalive;

	@Autowired
	robotService service;

	// 将MqttConnectOptions注入到容器中
	@Bean
	public MqttConnectOptions getMqttConnectOptions() {
		MqttConnectOptions mOptions = new MqttConnectOptions();
		mOptions.setUserName(username);
		mOptions.setPassword(password.toCharArray());
		mOptions.setServerURIs(new String[] { hostUrl });
		mOptions.setKeepAliveInterval(keepalive);
		return mOptions;
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(getMqttConnectOptions());
		return factory;
	}

	// 接收通道
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	/**
	 * 通道适配器
	 * 
	 * @param clientFactory
	 * @param mqttInputChannel
	 * @return
	 */
	@Bean
	public MessageProducer inbound(MqttPahoClientFactory clientFactory, MessageChannel mqttInputChannel) {
		// clientId 客户端ID，生产端和消费端的客户端ID需不同，不然服务器会认为是同一个客户端，会接收不到信息
		// defaultTopic 订阅的主题
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, clientFactory,
				defaultTopic);
		// 超时时间
		adapter.setCompletionTimeout(completionTimeout);
		// 转换器
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel);
		return adapter;
	}

	/**
	 * 消息处理
	 * 
	 * @return
	 */
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {
			public void handleMessage(Message<?> message) throws MessagingException {
				JSONObject json = JSONObject.fromObject(message.getPayload());
				//通过迭代器,遍历jsonObject的元素
				Iterator iterator = json.keys();  
				Map<String, String> map = new HashMap<String, String>();
				while(iterator.hasNext()){  
				       String key = (String) iterator.next();  
				       String value = json.getString(key);//这里可以根据实际类型去获取
				       map.put(key, value);
				}  
				service.updateCoordsByMqtt(map);
			}
		};
	}
}
