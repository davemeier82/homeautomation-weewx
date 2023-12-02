package io.github.davemeier82.homeautomation.weewx.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.davemeier82.homeautomation.core.device.mqtt.DefaultMqttSubscriber;
import io.github.davemeier82.homeautomation.core.device.property.*;
import io.github.davemeier82.homeautomation.core.device.property.defaults.*;
import io.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import io.github.davemeier82.homeautomation.core.event.EventPublisher;
import io.github.davemeier82.homeautomation.core.event.factory.EventFactory;
import io.github.davemeier82.homeautomation.weewx.WeewxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Long.parseLong;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.comparingLong;

public class WeewxDevice extends DefaultMqttSubscriber {
  private static final Logger log = LoggerFactory.getLogger(WeewxDevice.class);

  private final String topic;
  public static final String TYPE = "weewx";

  private final DefaultTemperatureSensor indoorTemperature;
  private final DefaultTemperatureSensor outdoorTemperature;
  private final DefaultTemperatureSensor apparentTemperature;
  private final DefaultTemperatureSensor extraTemperature;
  private final DefaultTemperatureSensor windchillTemperature;
  private final DefaultTemperatureSensor indoorDewpointTemperature;
  private final DefaultTemperatureSensor outdoorDewpointTemperature;
  private final DefaultHumiditySensor indoorHumitdity;
  private final DefaultHumiditySensor outdoorHumitdity;
  private final DefaultHumiditySensor extraHumitdity;
  private final DefaultPressureSensor airPressure;
  private final DefaultPressureSensor altimeter;
  private final DefaultPressureSensor barometer;
  private final DefaultCloudBaseSensor cloudBaseSensor;
  private final DefaultRainSensor rainSensor;
  private final DefaultIlluminanceSensor illuminanceSensor;
  private final DefaultUvSensor uvSensor;
  private final DefaultWindSensor windSensor;
  private final ObjectMapper objectMapper;
  private final Set<DeviceProperty> deviceProperties = ConcurrentHashMap.newKeySet();
  private final String fullTopic;

  /**
   * Constructor
   *
   * @param displayName       the display name
   * @param customIdentifiers optional custom identifier
   */
  public WeewxDevice(String topic,
                        String displayName,
                        ObjectMapper objectMapper,
                        EventPublisher eventPublisher,
                        EventFactory eventFactory,
                        Map<String, String> customIdentifiers
  ) {
    super(displayName, customIdentifiers);
    this.topic = topic;
    fullTopic = topic + "/loop";
    this.objectMapper = objectMapper;
    long i = 0;
    indoorTemperature = new DefaultTemperatureSensor(i++, "indoorTemperature", this, eventPublisher, eventFactory);
    outdoorTemperature = new DefaultTemperatureSensor(i++, "outdoorTemperature", this, eventPublisher, eventFactory);
    apparentTemperature = new DefaultTemperatureSensor(i++, "apparentTemperature", this, eventPublisher, eventFactory);
    extraTemperature = new DefaultTemperatureSensor(i++, "extraTemperature", this, eventPublisher, eventFactory);
    windchillTemperature = new DefaultTemperatureSensor(i++, "windchillTemperature", this, eventPublisher, eventFactory);
    indoorDewpointTemperature = new DefaultTemperatureSensor(i++, "indoorDewpointTemperature", this, eventPublisher, eventFactory);
    outdoorDewpointTemperature = new DefaultTemperatureSensor(i++, "outdoorDewpointTemperature", this, eventPublisher, eventFactory);
    indoorHumitdity = new DefaultHumiditySensor(i++, "indoorHumitdity", this, eventPublisher, eventFactory);
    outdoorHumitdity = new DefaultHumiditySensor(i++, "outdoorHumitdity", this, eventPublisher, eventFactory);
    extraHumitdity = new DefaultHumiditySensor(i++, "extraHumitdity", this, eventPublisher, eventFactory);
    airPressure = new DefaultPressureSensor(i++, "airPressure", this, eventPublisher, eventFactory);
    altimeter = new DefaultPressureSensor(i++, "altimeter", this, eventPublisher, eventFactory);
    barometer = new DefaultPressureSensor(i++, "barometer", this, eventPublisher, eventFactory);
    cloudBaseSensor = new DefaultCloudBaseSensor(i++, this, eventPublisher, eventFactory);
    rainSensor = new DefaultRainSensor(i++, this, eventPublisher, eventFactory);
    uvSensor = new DefaultUvSensor(i++, this, eventPublisher, eventFactory);
    windSensor = new DefaultWindSensor(i++, this, eventPublisher, eventFactory);
    illuminanceSensor = new DefaultIlluminanceSensor(i, this, eventPublisher, eventFactory);
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public String getId() {
    return topic;
  }

  @Override
  public List<? extends DeviceProperty> getDeviceProperties() {
    return deviceProperties.stream().sorted(comparingLong(DeviceProperty::getId)).toList();
  }

  @Override
  public String getTopic() {
    return fullTopic;
  }

  @Override
  public void processMessage(String topic, Optional<ByteBuffer> payload) {
    payload.ifPresent(byteBuffer -> {
      String message = UTF_8.decode(byteBuffer).toString();
      log.debug("{}: {}", topic, message);
      try {
        WeewxMessage weewxMessage = objectMapper.readValue(message, WeewxMessage.class);
        ZonedDateTime dateTime = ZonedDateTime.now();
        if (weewxMessage.getDateTime() != null) {
          Instant instant = Instant.ofEpochSecond(parseLong(weewxMessage.getDateTime()));
          dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        if (weewxMessage.getInTempC() != null) {
          deviceProperties.add(indoorTemperature);
          indoorTemperature.setTemperatureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getInTempC())));
        }
        if (weewxMessage.getOutTempC() != null) {
          deviceProperties.add(outdoorTemperature);
          outdoorTemperature.setTemperatureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getOutTempC())));
        }
        if (weewxMessage.getAppTempC() != null) {
          deviceProperties.add(apparentTemperature);
          apparentTemperature.setTemperatureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getAppTempC())));
        }
        if (weewxMessage.getAppTempC() != null) {
          deviceProperties.add(extraTemperature);
          extraTemperature.setTemperatureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getExtraTemp2C())));
        }
        if (weewxMessage.getWindchillC() != null) {
          deviceProperties.add(windchillTemperature);
          windchillTemperature.setTemperatureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getWindchillC())));
        }
        if (weewxMessage.getInDewpointC() != null) {
          deviceProperties.add(indoorDewpointTemperature);
          indoorDewpointTemperature.setTemperatureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getInDewpointC())));
        }
        if (weewxMessage.getDewpointC() != null) {
          deviceProperties.add(outdoorDewpointTemperature);
          outdoorDewpointTemperature.setTemperatureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getDewpointC())));
        }
        if (weewxMessage.getInHumidity() != null) {
          deviceProperties.add(indoorHumitdity);
          indoorHumitdity.setRelativeHumidityInPercent(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getInHumidity())));
        }
        if (weewxMessage.getOutHumidity() != null) {
          deviceProperties.add(outdoorHumitdity);
          outdoorHumitdity.setRelativeHumidityInPercent(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getOutHumidity())));
        }
        if (weewxMessage.getExtraHumid2() != null) {
          deviceProperties.add(extraHumitdity);
          extraHumitdity.setRelativeHumidityInPercent(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getExtraHumid2())));
        }
        if (weewxMessage.getPressureMbar() != null) {
          deviceProperties.add(airPressure);
          airPressure.setPressureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getPressureMbar())));
        }
        if (weewxMessage.getBarometerMbar() != null) {
          deviceProperties.add(barometer);
          barometer.setPressureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getBarometerMbar())));
        }
        if (weewxMessage.getAltimeterMbar() != null) {
          deviceProperties.add(altimeter);
          altimeter.setPressureInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getAltimeterMbar())));
        }
        if (weewxMessage.getCloudbaseMeter() != null) {
          deviceProperties.add(cloudBaseSensor);
          cloudBaseSensor.setCloudBaseInDegree(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getCloudbaseMeter())));
        }
        if (weewxMessage.getRainCm() != null) {
          deviceProperties.add(rainSensor);
          rainSensor.setIntervalAmount(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getRainCm()) * 10.0f));
        }
        if (weewxMessage.getDayRainCm() != null) {
          deviceProperties.add(rainSensor);
          rainSensor.setTodayAmount(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getDayRainCm()) * 10.0f));
        }
        if (weewxMessage.getRainRateCmPerHour() != null) {
          deviceProperties.add(rainSensor);
          rainSensor.setRate(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getRainRateCmPerHour()) * 10.0f));
        }
        if (weewxMessage.getLuminosity() != null) {
          deviceProperties.add(illuminanceSensor);
          illuminanceSensor.setIlluminanceInLux(new DataWithTimestamp<>(dateTime, Float.valueOf(weewxMessage.getLuminosity()).intValue()));
        }
        if (weewxMessage.getUV() != null) {
          deviceProperties.add(uvSensor);
          uvSensor.setUvIndex(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getUV())));
        }
        if (weewxMessage.getWindSpeedKmh() != null) {
          deviceProperties.add(windSensor);
          windSensor.setSpeed(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getWindSpeedKmh())));
        }
        if (weewxMessage.getWindDir() != null) {
          deviceProperties.add(windSensor);
          windSensor.setDirection(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getWindDir())));
        }
        if (weewxMessage.getWindGustKmh() != null) {
          deviceProperties.add(windSensor);
          windSensor.setGustSpeed(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getWindGustKmh())));
        }
        if (weewxMessage.getWindGustDir() != null) {
          deviceProperties.add(windSensor);
          windSensor.setGustDirection(new DataWithTimestamp<>(dateTime, parseFloat(weewxMessage.getWindGustDir())));
        }
        if (weewxMessage.getWindrunKm() != null) {
          deviceProperties.add(windSensor);
          windSensor.setRun(new DataWithTimestamp<>(dateTime, parseDouble(weewxMessage.getWindrunKm())));
        }
      } catch (JsonProcessingException e) {
        throw new UncheckedIOException(e);
      }
    });
  }
}