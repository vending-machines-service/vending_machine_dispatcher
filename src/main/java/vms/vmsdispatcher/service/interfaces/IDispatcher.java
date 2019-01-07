package vms.vmsdispatcher.service.interfaces;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;

public interface IDispatcher extends Sink {

	String MAINTENANCE = "maintenance";
	String MALFUNCTION = "malfunction";
	String COMMON = "common";

	@Output(MAINTENANCE)
	MessageChannel sendMaintenanceChannel();

	@Output(MALFUNCTION)
	MessageChannel sendMalfunctionChannel();

	@Output(COMMON)
	MessageChannel sendCommonChannel();
}
