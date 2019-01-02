package vending.m2m.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vending.m2m.dto.VendingSensorData;

@EnableBinding(IDispatcher.class)
public class Dispatcher {

	ObjectMapper mapper = new ObjectMapper();

	@Value("${min_product_value:20}")
	int minProductValue;

	@Value("${max_money_value:150}")
	int maxMoneyValue;

	@Autowired
	IDispatcher channels;

	@StreamListener(IDispatcher.INPUT)
	void getSensorData(String jsonSensor) throws JsonParseException, JsonMappingException, IOException {
		VendingSensorData sensor = mapper.readValue(jsonSensor, VendingSensorData.class);
	
		if (sensor.sensorId < 200 && sensor.value <= minProductValue)
			channels.maintenance().send(MessageBuilder.withPayload(jsonSensor).build());
		else if (sensor.sensorId >= 200 && sensor.value >= maxMoneyValue)
			channels.maintenance().send(MessageBuilder.withPayload(jsonSensor).build());
		else if (sensor.sensorId >= 600 && sensor.value == 1)
			channels.malfunction().send(MessageBuilder.withPayload(jsonSensor).build());
		
		channels.normal().send(MessageBuilder.withPayload(jsonSensor).build());
	}
}
