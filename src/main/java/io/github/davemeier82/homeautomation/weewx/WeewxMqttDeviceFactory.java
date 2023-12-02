/*
 * Copyright 2021-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.davemeier82.homeautomation.weewx;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.device.mqtt.MqttDeviceFactory;
import io.github.davemeier82.homeautomation.core.device.mqtt.MqttSubscriber;
import io.github.davemeier82.homeautomation.core.event.EventPublisher;
import io.github.davemeier82.homeautomation.core.event.factory.EventFactory;
import io.github.davemeier82.homeautomation.core.mqtt.MqttClient;
import io.github.davemeier82.homeautomation.weewx.device.WeewxDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Factory for Weewx devices
 *
 * @author David Meier
 * @since 0.1.0
 */
public class WeewxMqttDeviceFactory implements MqttDeviceFactory {
  private static final Logger log = LoggerFactory.getLogger(WeewxMqttDeviceFactory.class);
  private final EventPublisher eventPublisher;
  private final EventFactory eventFactory;
  private final MqttClient mqttClient;
  private final ObjectMapper objectMapper;

  /**
   * Constructor.
   *
   * @param eventPublisher the event publisher
   * @param eventFactory   the event factory
   * @param mqttClient     the MQTT client
   * @param objectMapper   the object mapper
   */
  public WeewxMqttDeviceFactory(EventPublisher eventPublisher,
                                EventFactory eventFactory,
                                MqttClient mqttClient,
                                ObjectMapper objectMapper
  ) {
    this.eventPublisher = eventPublisher;
    this.eventFactory = eventFactory;
    this.mqttClient = mqttClient;
    this.objectMapper = objectMapper;
  }

  @Override
  public boolean supportsDeviceType(String type) {
    return type.equalsIgnoreCase(WeewxDevice.TYPE);
  }

  @Override
  public Set<String> getSupportedDeviceTypes() {
    return Set.of(WeewxDevice.TYPE, WeewxDevice.TYPE.toUpperCase());
  }

  @Override
  public MqttSubscriber createDevice(String type,
                                     String id,
                                     String displayName,
                                     Map<String, String> parameters,
                                     Map<String, String> customIdentifiers
  ) {
    if (supportsDeviceType(type)) {
      WeewxDevice device = new WeewxDevice("weather/loop", displayName, objectMapper, eventPublisher, eventFactory, customIdentifiers);
      log.debug("creating WeewxDevice device with id {} ({})", id, displayName);
      mqttClient.subscribe(device.getTopic(), device::processMessage);

      return device;
    }
    throw new IllegalArgumentException("device type '" + type + "' not supported");
  }

  @Override
  public String getRootTopic() {
    return "weather/";
  }

  @Override
  public Optional<DeviceId> getDeviceId(String topic) {
    return Optional.of(new DeviceId("weather", WeewxDevice.TYPE));
  }

  @Override
  public Optional<MqttSubscriber> createMqttSubscriber(DeviceId deviceId) {
    try {
      return Optional.of(createDevice(deviceId.type(), deviceId.id(), deviceId.toString(), Map.of(), Map.of()));
    } catch (IllegalArgumentException e) {
      log.debug("unknown device with id: {}", deviceId, e);
      return Optional.empty();
    }
  }
}
