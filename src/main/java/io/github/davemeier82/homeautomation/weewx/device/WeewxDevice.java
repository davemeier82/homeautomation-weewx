package io.github.davemeier82.homeautomation.weewx.device;

import io.github.davemeier82.homeautomation.core.device.DeviceType;
import io.github.davemeier82.homeautomation.core.device.mqtt.AbstractDevice;

import java.util.Map;

import static io.github.davemeier82.homeautomation.weewx.device.WeewxDeviceType.WEEWX;

public class WeewxDevice extends AbstractDevice {
  private final String topic;

  public WeewxDevice(String topic,
                     String displayName,
                     Map<String, String> customIdentifiers
  ) {
    super(displayName, customIdentifiers);
    this.topic = topic;
  }

  @Override
  public DeviceType getType() {
    return WEEWX;
  }

  @Override
  public String getId() {
    return topic;
  }

}