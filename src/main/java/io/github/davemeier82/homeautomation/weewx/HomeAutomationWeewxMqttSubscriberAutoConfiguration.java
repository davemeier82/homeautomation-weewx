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
import io.github.davemeier82.homeautomation.spring.core.HomeAutomationCorePersistenceAutoConfiguration;
import io.github.davemeier82.homeautomation.spring.core.HomeAutomationCoreValueUpdateServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({HomeAutomationCoreValueUpdateServiceAutoConfiguration.class, HomeAutomationCorePersistenceAutoConfiguration.class, JacksonAutoConfiguration.class})
public class HomeAutomationWeewxMqttSubscriberAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({ObjectMapper.class, TemperatureValueUpdateService.class, HumidityValueUpdateService.class, PressureValueUpdateService.class, CloudBaseValueUpdateService.class,
      RainIntervalValueUpdateService.class, RainTodayValueUpdateService.class, RainRateValueUpdateService.class, IlluminanceValueUpdateService.class, UvIndexValueUpdateService.class,
      WindSpeedValueUpdateService.class, WindDirectionValueUpdateService.class, WindGustSpeedValueUpdateService.class, WindGustDirectionValueUpdateService.class, WindRunValueUpdateService.class,
      LightningDistanceValueUpdateService.class, LightningCountValueUpdateService.class})
  WeewxMqttSubscriber weewxMqttSubscriber(ObjectMapper objectMapper,
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
    return new WeewxMqttSubscriber(objectMapper, temperatureValueUpdateService, humidityValueUpdateService, pressureValueUpdateService, cloudBaseValueUpdateService, rainIntervalValueUpdateService,
        rainTodayValueUpdateService, rainRateValueUpdateService, illuminanceValueUpdateService, uvIndexValueUpdateService, windSpeedValueUpdateService, windDirectionValueUpdateService,
        windGustSpeedValueUpdateService, windGustDirectionValueUpdateService, windRunValueUpdateService, lightningCountValueUpdateService, lightningDistanceValueUpdateService, deviceRepository,
        weewxDeviceFactory);
  }

}
