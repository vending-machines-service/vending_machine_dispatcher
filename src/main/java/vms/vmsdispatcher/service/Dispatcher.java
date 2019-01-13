package vms.vmsdispatcher.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;

import lombok.extern.slf4j.Slf4j;
import vms.vmsdispatcher.configuration.DispatcherParams;
import vms.vmsdispatcher.dto.SensorDTO;
import vms.vmsdispatcher.dto.SensorTypeEnum;
import vms.vmsdispatcher.service.interfaces.IDispatcher;
import vms.vmsdispatcher.service.interfaces.ISensors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableBinding(IDispatcher.class)
@Slf4j
public class Dispatcher {
  @Autowired
  ISensors sensorService;
  @Autowired
  DispatcherParams params;

  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  IDispatcher channels;

  @StreamListener(IDispatcher.INPUT)
  void getSensorData(String jsonSensor) throws JsonParseException, JsonMappingException, IOException {
    SensorDTO sensor = mapper.readValue(jsonSensor, SensorDTO.class);

    SensorTypeEnum sensorType = this.sensorService.getSensorType(sensor.getSensorId());
    channels.sendCommonChannel().send(MessageBuilder.withPayload(jsonSensor).build());
    log.info("[TOPIC]:COMMON({})      => [MACHINE: {}; SENSOR:{}; VALUE: {}] ", sensorType, sensor.getMachineId(),
        sensor.getSensorId(), sensor.getValue());

    if (sensorType == SensorTypeEnum.PRODUCT) {
      channels.sendProductsChannel().send(MessageBuilder.withPayload(jsonSensor).build());
      log.info("[TOPIC]:PRODUCT({})   => [MACHINE: {}; SENSOR:{}; VALUE: {}] ", sensorType, sensor.getMachineId(),
          sensor.getSensorId(), sensor.getValue());
    }

    if (sensorType == SensorTypeEnum.CRASH && sensor.getValue() == 1) {
      this.channels.sendMalfunctionChannel().send(MessageBuilder.withPayload(jsonSensor).build());
      log.info("[TOPIC]:MALFUNCTION({}) => [MACHINE: {}; SENSOR:{}; VALUE: {}] ", sensorType, sensor.getMachineId(),
          sensor.getSensorId(), sensor.getValue());
    } else if ((sensorType == SensorTypeEnum.INCREASE && this.isIncreaseExtreme(sensor.getValue()))
        || ((sensorType == SensorTypeEnum.DECREASE || sensorType == SensorTypeEnum.PRODUCT)
            && this.isDecreaseExtreme(sensor.getValue()))) {
      log.info("[TOPIC]:MAINTENANCE({}) => [MACHINE: {}; SENSOR:{}; VALUE: {}] ", sensorType, sensor.getMachineId(),
          sensor.getSensorId(), sensor.getValue());
      this.channels.sendMaintenanceChannel().send(MessageBuilder.withPayload(jsonSensor).build());
    } else if (sensorType == SensorTypeEnum.NOT_MAPPED) {
      log.error("[UNREGISTERED SENSOR]({}) => [MACHINE: {}; SENSOR:{}; VALUE: {}] ", sensorType, sensor.getMachineId(),
          sensor.getSensorId(), sensor.getValue());
    }
  }

  private boolean isIncreaseExtreme(int value) {
    int maxValue = this.params.getMaxValue();
    int alertPercentage = this.params.getAlertPercentage();
    float currentPercentage = value * 100f / maxValue;
    if (currentPercentage > 100 - alertPercentage) {
      return true;
    }
    return false;
  }

  private boolean isDecreaseExtreme(int value) {
    int maxValue = this.params.getMaxValue();
    int alertPercentage = this.params.getAlertPercentage();
    float currentPercentage = value * 100f / maxValue;
    if (currentPercentage < alertPercentage) {
      return true;
    }
    return false;
  }
}
