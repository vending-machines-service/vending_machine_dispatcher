package vms.vmsdispatcher.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SensorDTO {

  private int machineId;
  private int sensorId;
  private int value;

  public SensorDTO(int machineId, int sensorId, int value) {
    super();
    this.machineId = machineId;
    this.sensorId = sensorId;
    this.value = value;
  }
}
