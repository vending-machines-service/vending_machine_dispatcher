package vms.vmsdispatcher.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vms.vmsdispatcher.configuration.DispatcherParams;
import vms.vmsdispatcher.configuration.SensorProps;
import vms.vmsdispatcher.dto.SensorTypeEnum;
import vms.vmsdispatcher.service.interfaces.ISensors;

@Service
@Slf4j
public class SensorsService implements ISensors {

  @Autowired
  DispatcherParams params;

  @Override
  public SensorTypeEnum getSensorType(int sensorId) {
    Map<String, SensorProps> ranges = this.params.getSensorRanges();
    if (
      sensorId >= ranges.get("DECREASE").getFrom() && 
      sensorId <= ranges.get("DECREASE").getTo()) {
      // log.info("MAPPED DECREASE SENSOR WITH ID: {}", sensorId);
      return SensorTypeEnum.DECREASE;
    } else if (
      sensorId >= ranges.get("CRASH").getFrom() && 
      sensorId <= ranges.get("CRASH").getTo()) {
      // log.info("MAPPED CRASH SENSOR WITH ID: {}", sensorId);
      return SensorTypeEnum.CRASH;
    } else if (
      sensorId >= ranges.get("INCREASE").getFrom() && 
      sensorId <= ranges.get("INCREASE").getTo()) {
      // log.info("MAPPED INCREASE SENSOR WITH ID: {}", sensorId);
      return SensorTypeEnum.INCREASE;
    } else {
      log.error("SENSOR WITH ID: {} IS NOT MAPPED", sensorId);
      return SensorTypeEnum.NOT_MAPPED;
    }
  }
}