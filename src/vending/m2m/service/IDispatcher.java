package vending.m2m.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;

public interface IDispatcher extends Sink{

	String MAINTENANCE = "maintenance";
	String MALFUNCTION = "malfunction";
	String NORMAL = "normal";
	
	@Output(MAINTENANCE)
	MessageChannel maintenance();
	
	@Output(MALFUNCTION)
	MessageChannel malfunction();
	
	@Output(NORMAL)
	MessageChannel normal();
}
