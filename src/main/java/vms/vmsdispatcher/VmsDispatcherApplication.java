package vms.vmsdispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
@SpringBootApplication
public class VmsDispatcherApplication {

	private static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		ctx = SpringApplication.run(VmsDispatcherApplication.class, args);
	}

	@ManagedOperation
	public static void stop() {
		ctx.close();
	}

}

