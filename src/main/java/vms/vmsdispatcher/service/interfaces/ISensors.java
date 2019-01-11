package vms.vmsdispatcher.service.interfaces;

import vms.vmsdispatcher.dto.SensorTypeEnum;

public interface ISensors {
  /**
   * Returns sensor type depending on given sensor id;
   * @param sensorId
   * @return
   */
  public SensorTypeEnum getSensorType(int sensorId);
}