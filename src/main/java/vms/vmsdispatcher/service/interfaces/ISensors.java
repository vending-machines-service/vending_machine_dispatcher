package vms.vmsdispatcher.service.interfaces;

import vms.vmsdispatcher.dto.SensorTypeEnum;

public interface ISensors {
  public SensorTypeEnum getSensorType(int sensorId);
}