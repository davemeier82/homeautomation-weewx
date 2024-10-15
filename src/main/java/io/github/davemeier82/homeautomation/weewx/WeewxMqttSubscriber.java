/*
 * Copyright 2021-2024 the original author or authors.
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.davemeier82.homeautomation.core.device.Device;
import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.device.mqtt.MqttSubscriber;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyId;
import io.github.davemeier82.homeautomation.core.repositories.DeviceRepository;
import io.github.davemeier82.homeautomation.core.updater.CloudBaseValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.HumidityValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.IlluminanceValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.LightningCountValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.LightningDistanceValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.PressureValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RainIntervalValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RainRateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RainTodayValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.TemperatureValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.UvIndexValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindDirectionValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindGustDirectionValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindGustSpeedValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindRunValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindSpeedValueUpdateService;
import io.github.davemeier82.homeautomation.weewx.device.WeewxDeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.nio.charset.StandardCharsets.UTF_8;

public class WeewxMqttSubscriber implements MqttSubscriber {
  private static final Logger log = LoggerFactory.getLogger(WeewxMqttSubscriber.class);
  private final ObjectMapper objectMapper;

  private final DeviceId DEVICE_ID = new DeviceId("weather", WeewxDeviceType.WEEWX);

  private final TemperatureValueUpdateService temperatureValueUpdateService;
  private final HumidityValueUpdateService humidityValueUpdateService;
  private final PressureValueUpdateService pressureValueUpdateService;
  private final CloudBaseValueUpdateService cloudBaseValueUpdateService;
  private final RainIntervalValueUpdateService rainIntervalValueUpdateService;
  private final RainTodayValueUpdateService rainTodayValueUpdateService;
  private final RainRateValueUpdateService rainRateValueUpdateService;
  private final IlluminanceValueUpdateService illuminanceValueUpdateService;
  private final UvIndexValueUpdateService uvIndexValueUpdateService;
  private final WindSpeedValueUpdateService windSpeedValueUpdateService;
  private final WindDirectionValueUpdateService windDirectionValueUpdateService;
  private final WindGustSpeedValueUpdateService windGustSpeedValueUpdateService;
  private final WindGustDirectionValueUpdateService windGustDirectionValueUpdateService;
  private final WindRunValueUpdateService windRunValueUpdateService;
  private final LightningCountValueUpdateService lightningCountValueUpdateService;
  private final LightningDistanceValueUpdateService lightningDistanceValueUpdateService;
  private final DeviceRepository deviceRepository;
  private final WeewxDeviceFactory weewxDeviceFactory;

  public WeewxMqttSubscriber(
      ObjectMapper objectMapper,
      TemperatureValueUpdateService temperatureValueUpdateService,
      HumidityValueUpdateService humidityValueUpdateService,
      PressureValueUpdateService pressureValueUpdateService,
      CloudBaseValueUpdateService cloudBaseValueUpdateService,
      RainIntervalValueUpdateService rainIntervalValueUpdateService,
      RainTodayValueUpdateService rainTodayValueUpdateService,
      RainRateValueUpdateService rainRateValueUpdateService,
      IlluminanceValueUpdateService illuminanceValueUpdateService,
      UvIndexValueUpdateService uvIndexValueUpdateService,
      WindSpeedValueUpdateService windSpeedValueUpdateService,
      WindDirectionValueUpdateService windDirectionValueUpdateService,
      WindGustSpeedValueUpdateService windGustSpeedValueUpdateService,
      WindGustDirectionValueUpdateService windGustDirectionValueUpdateService,
      WindRunValueUpdateService windRunValueUpdateService,
      LightningCountValueUpdateService lightningCountValueUpdateService,
      LightningDistanceValueUpdateService lightningDistanceValueUpdateService,
      DeviceRepository deviceRepository,
      WeewxDeviceFactory weewxDeviceFactory
  ) {
    this.temperatureValueUpdateService = temperatureValueUpdateService;
    this.humidityValueUpdateService = humidityValueUpdateService;
    this.pressureValueUpdateService = pressureValueUpdateService;
    this.cloudBaseValueUpdateService = cloudBaseValueUpdateService;
    this.rainIntervalValueUpdateService = rainIntervalValueUpdateService;
    this.rainTodayValueUpdateService = rainTodayValueUpdateService;
    this.rainRateValueUpdateService = rainRateValueUpdateService;
    this.illuminanceValueUpdateService = illuminanceValueUpdateService;
    this.uvIndexValueUpdateService = uvIndexValueUpdateService;
    this.windSpeedValueUpdateService = windSpeedValueUpdateService;
    this.windDirectionValueUpdateService = windDirectionValueUpdateService;
    this.windGustSpeedValueUpdateService = windGustSpeedValueUpdateService;
    this.windGustDirectionValueUpdateService = windGustDirectionValueUpdateService;
    this.windRunValueUpdateService = windRunValueUpdateService;
    this.lightningCountValueUpdateService = lightningCountValueUpdateService;
    this.lightningDistanceValueUpdateService = lightningDistanceValueUpdateService;
    this.objectMapper = objectMapper;
    this.deviceRepository = deviceRepository;
    this.weewxDeviceFactory = weewxDeviceFactory;
  }

  @Override
  public String getTopic() {
    return "weather/loop/#";
  }

  @Override
  public void processMessage(String topic, Optional<ByteBuffer> payload) {
    payload.ifPresent(byteBuffer -> {
      String message = UTF_8.decode(byteBuffer).toString();
      log.debug("{}: {}", topic, message);
      deviceRepository.getByDeviceId(DEVICE_ID).orElseGet(() -> {
        Device newDevice = weewxDeviceFactory.createDevice(DEVICE_ID.type(), DEVICE_ID.id(), DEVICE_ID.toString(), Map.of(), Map.of()).orElseThrow();
        deviceRepository.save(newDevice);
        return newDevice;
      });
      try {
        WeewxMessage weewxMessage = objectMapper.readValue(message, WeewxMessage.class);
        OffsetDateTime dateTime = OffsetDateTime.now();
        if (weewxMessage.getDateTime() != null) {
          Instant instant = Instant.ofEpochSecond(Double.valueOf(weewxMessage.getDateTime()).longValue());
          dateTime = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        if (weewxMessage.getInTempC() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getInTempC()), dateTime, createId("indoorTemperature"), "Indoor Temperature");
        }
        if (weewxMessage.getOutTempC() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getOutTempC()), dateTime, createId("outdoorTemperature"), "Outdoor Temperature");
        }
        if (weewxMessage.getAppTempC() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getAppTempC()), dateTime, createId("apparentTemperature"), "Apparent Temperature");
        }
        if (weewxMessage.getExtraTemp2C() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getExtraTemp2C()), dateTime, createId("extraTemperature2"), "Extra Temperature 2");
        }
        if (weewxMessage.getExtraTemp3C() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getExtraTemp3C()), dateTime, createId("extraTemperature3"), "Extra Temperature 3");
        }
        if (weewxMessage.getExtraTemp4C() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getExtraTemp4C()), dateTime, createId("extraTemperature4"), "Extra Temperature 4");
        }
        if (weewxMessage.getExtraTemp5C() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getExtraTemp5C()), dateTime, createId("extraTemperature5"), "Extra Temperature 5");
        }
        if (weewxMessage.getWindchillC() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getWindchillC()), dateTime, createId("windchillTemperature"), "Windchill Temperature 2");
        }
        if (weewxMessage.getInDewpointC() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getInDewpointC()), dateTime, createId("indoorDewpointTemperature"), "Indoor Dewpoint Temperature 2");
        }
        if (weewxMessage.getDewpointC() != null) {
          temperatureValueUpdateService.setValue(parseFloat(weewxMessage.getDewpointC()), dateTime, createId("outdoorDewpointTemperature"), "Outdoor Dewpoint Temperature 2");
        }
        if (weewxMessage.getInHumidity() != null) {
          humidityValueUpdateService.setValue(parseFloat(weewxMessage.getInHumidity()), dateTime, createId("indoorHumidity"), "Indoor Humidity");
        }
        if (weewxMessage.getOutHumidity() != null) {
          humidityValueUpdateService.setValue(parseFloat(weewxMessage.getOutHumidity()), dateTime, createId("outdoorHumidity"), "Outdoor Humidity");
        }
        if (weewxMessage.getExtraHumid2() != null) {
          humidityValueUpdateService.setValue(parseFloat(weewxMessage.getExtraHumid2()), dateTime, createId("extraHumidity2"), "Extra Humidity 2");
        }
        if (weewxMessage.getExtraHumid3() != null) {
          humidityValueUpdateService.setValue(parseFloat(weewxMessage.getExtraHumid3()), dateTime, createId("extraHumidity3"), "Extra Humidity 3");
        }
        if (weewxMessage.getExtraHumid4() != null) {
          humidityValueUpdateService.setValue(parseFloat(weewxMessage.getExtraHumid4()), dateTime, createId("extraHumidity4"), "Extra Humidity 4");
        }
        if (weewxMessage.getExtraHumid5() != null) {
          humidityValueUpdateService.setValue(parseFloat(weewxMessage.getExtraHumid5()), dateTime, createId("extraHumidity5"), "Extra Humidity 5");
        }
        if (weewxMessage.getPressureMbar() != null) {
          pressureValueUpdateService.setValue(parseFloat(weewxMessage.getPressureMbar()), dateTime, createId("airPressure"), "Air Pressure");
        }
        if (weewxMessage.getBarometerMbar() != null) {
          pressureValueUpdateService.setValue(parseFloat(weewxMessage.getBarometerMbar()), dateTime, createId("barometer"), "Barometer");
        }
        if (weewxMessage.getAltimeterMbar() != null) {
          pressureValueUpdateService.setValue(parseFloat(weewxMessage.getAltimeterMbar()), dateTime, createId("altimeter"), "Altimeter");
        }
        if (weewxMessage.getCloudbaseMeter() != null) {
          cloudBaseValueUpdateService.setValue(parseFloat(weewxMessage.getCloudbaseMeter()), dateTime, createId("cloudBase"), "Cloud Base");
        }
        if (weewxMessage.getRainCm() != null) {
          rainIntervalValueUpdateService.setValue(parseFloat(weewxMessage.getRainCm()) * 10.0f, dateTime, createId("rain"), "Rain Interval");
        }
        if (weewxMessage.getDayRainCm() != null) {
          rainTodayValueUpdateService.setValue(parseFloat(weewxMessage.getDayRainCm()) * 10.0f, dateTime, createId("rain"), "Rain Today");
        }
        if (weewxMessage.getRainRateCmPerHour() != null) {
          rainRateValueUpdateService.setValue(parseFloat(weewxMessage.getRainRateCmPerHour()) * 10.0f, dateTime, createId("rain"), "Rain Rate");
        }
        if (weewxMessage.getLuminosityLux() != null) {
          illuminanceValueUpdateService.setValue(Float.valueOf(weewxMessage.getLuminosityLux()).intValue(), dateTime, createId("illuminance"), "Illuminance");
        }
        if (weewxMessage.getUv() != null) {
          uvIndexValueUpdateService.setValue(parseFloat(weewxMessage.getUv()), dateTime, createId("uv"), "UV Index");
        }
        if (weewxMessage.getWindSpeedKmh() != null) {
          windSpeedValueUpdateService.setValue(parseFloat(weewxMessage.getWindSpeedKmh()), dateTime, createId("wind"), "Wind Speed");
        }
        if (weewxMessage.getWindDir() != null) {
          windDirectionValueUpdateService.setValue(parseFloat(weewxMessage.getWindDir()), dateTime, createId("wind"), "Wind Direction");
        }
        if (weewxMessage.getWindGustKmh() != null) {
          windGustSpeedValueUpdateService.setValue(parseFloat(weewxMessage.getWindGustKmh()), dateTime, createId("wind"), "Wind Gust Speed");
        }
        if (weewxMessage.getWindGustDir() != null) {
          windGustDirectionValueUpdateService.setValue(parseFloat(weewxMessage.getWindGustDir()), dateTime, createId("wind"), "Wind Gust Direction");
        }
        if (weewxMessage.getWindrunKm() != null) {
          windRunValueUpdateService.setValue(parseDouble(weewxMessage.getWindrunKm()), dateTime, createId("wind"), "Wind Run");
        }
        if (weewxMessage.getLightingDistanceKm() != null) {
          OffsetDateTime lightningDateTime = dateTime;
          if (weewxMessage.getLastLightningDateTime() != null) {
            Instant instant = Instant.ofEpochSecond(Double.valueOf(weewxMessage.getLastLightningDateTime()).longValue());
            lightningDateTime = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
          }
          lightningDistanceValueUpdateService.setValue(Math.round(parseFloat(weewxMessage.getLightingDistanceKm())), lightningDateTime, createId("lightning"), "Lightning Distance");
        }
        if (weewxMessage.getLightingStrikeCount() != null) {
          lightningCountValueUpdateService.setValue(Math.round(parseFloat(weewxMessage.getLightingStrikeCount())), dateTime, createId("lightning"), "Lightning Count");
        }
      } catch (JsonProcessingException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  private DevicePropertyId createId(String id) {
    return new DevicePropertyId(DEVICE_ID, id);
  }
}