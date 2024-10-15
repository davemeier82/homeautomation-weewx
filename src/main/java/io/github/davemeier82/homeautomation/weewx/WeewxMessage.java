package io.github.davemeier82.homeautomation.weewx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeewxMessage {
  private String dateTime;
  @JsonProperty("inTemp_C")
  private String inTempC;
  @JsonProperty("outTemp_C")
  private String outTempC;
  private String inHumidity;
  private String outHumidity;
  @JsonProperty("pressure_mbar")
  private String pressureMbar;
  private String relbarometer;
  @JsonProperty("wh32_batt")
  private String wh32Battery;
  @JsonProperty("wh32_sig")
  private String wh32Signal;
  @JsonProperty("altimeter_mbar")
  private String altimeterMbar;
  @JsonProperty("appTemp_C")
  private String appTempC;
  @JsonProperty("barometer_mbar")
  private String barometerMbar;
  @JsonProperty("cloudbase_meter")
  private String cloudbaseMeter;
  @JsonProperty("dewpoint_C")
  private String dewpointC;
  @JsonProperty("heatindex_C")
  private String heatindexC;
  @JsonProperty("humidex_C")
  private String humidexC;
  @JsonProperty("inDewpoint_C")
  private String inDewpointC;
  @JsonProperty("rainRate_cm_per_hour")
  private String rainRateCmPerHour;
  @JsonProperty("windchill_C")
  private String windchillC;
  @JsonProperty("windrun_km")
  private String windrunKm;
  @JsonProperty("interval_minute")
  private String intervalMinute;
  @JsonProperty("hourRain_cm")
  private String hourRainCm;
  @JsonProperty("rain24_cm")
  private String rain24Cm;
  @JsonProperty("dayRain_cm")
  private String dayRainCm;
  private String usUnits;
  @JsonProperty("luminosity_lux")
  private String luminosityLux;
  private String uvradiation;

  @JsonProperty("UV")
  private String uv;
  @JsonProperty("rain_cm")
  private String rainCm;
  @JsonProperty("stormRain_cm")
  private String stormRainCm;
  @JsonProperty("weekRain_cm")
  private String weekRainCm;
  @JsonProperty("monthRain_cm")
  private String monthRainCm;
  @JsonProperty("yearRain_cm")
  private String yearRainCm;
  @JsonProperty("windSpeed_kph")
  private String windSpeedKmh;
  private String windDir;
  @JsonProperty("windGust_kph")
  private String windGustKmh;
  private String windGustDir;
  private String daymaxwind;
  @JsonProperty("wh65_sig")
  private String wh65Signal;
  @JsonProperty("extraTemp2_C")
  private String extraTemp2C;
  @JsonProperty("extraTemp3_C")
  private String extraTemp3C;
  @JsonProperty("extraTemp4_C")
  private String extraTemp4C;
  @JsonProperty("extraTemp5_C")
  private String extraTemp5C;

  private String extraHumid2;
  private String extraHumid3;
  private String extraHumid4;
  private String extraHumid5;
  @JsonProperty("wh31_ch2_batt")
  private String wh31Ch2Battery;
  @JsonProperty("wh31_ch2_sig")
  private String wh31Ch2Signal;
  @JsonProperty("wh40_batt")
  private String wh40Battery;
  @JsonProperty("wh40_sig")
  private String wh40Signal;
  @JsonProperty("wh68_batt")
  private String wh68Battery;
  @JsonProperty("wh68_sig")
  private String wh68Signal;

  @JsonProperty("lightning_distance_km")
  private String lightingDistanceKm;
  @JsonProperty("lightning_strike_count_count")
  private String lightingStrikeCount;
  @JsonProperty("lightning_last_det_time")
  private String lastLightningDateTime;

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public String getInTempC() {
    return inTempC;
  }

  public void setInTempC(String inTempC) {
    this.inTempC = inTempC;
  }

  public String getOutTempC() {
    return outTempC;
  }

  public void setOutTempC(String outTempC) {
    this.outTempC = outTempC;
  }

  public String getInHumidity() {
    return inHumidity;
  }

  public void setInHumidity(String inHumidity) {
    this.inHumidity = inHumidity;
  }

  public String getOutHumidity() {
    return outHumidity;
  }

  public void setOutHumidity(String outHumidity) {
    this.outHumidity = outHumidity;
  }

  public String getPressureMbar() {
    return pressureMbar;
  }

  public void setPressureMbar(String pressureMbar) {
    this.pressureMbar = pressureMbar;
  }

  public String getRelbarometer() {
    return relbarometer;
  }

  public void setRelbarometer(String relbarometer) {
    this.relbarometer = relbarometer;
  }

  public String getWh32Battery() {
    return wh32Battery;
  }

  public void setWh32Battery(String wh32Battery) {
    this.wh32Battery = wh32Battery;
  }

  public String getWh32Signal() {
    return wh32Signal;
  }

  public void setWh32Signal(String wh32Signal) {
    this.wh32Signal = wh32Signal;
  }

  public String getAltimeterMbar() {
    return altimeterMbar;
  }

  public void setAltimeterMbar(String altimeterMbar) {
    this.altimeterMbar = altimeterMbar;
  }

  public String getAppTempC() {
    return appTempC;
  }

  public void setAppTempC(String appTempC) {
    this.appTempC = appTempC;
  }

  public String getBarometerMbar() {
    return barometerMbar;
  }

  public void setBarometerMbar(String barometerMbar) {
    this.barometerMbar = barometerMbar;
  }

  public String getCloudbaseMeter() {
    return cloudbaseMeter;
  }

  public void setCloudbaseMeter(String cloudbaseMeter) {
    this.cloudbaseMeter = cloudbaseMeter;
  }

  public String getDewpointC() {
    return dewpointC;
  }

  public void setDewpointC(String dewpointC) {
    this.dewpointC = dewpointC;
  }

  public String getHeatindexC() {
    return heatindexC;
  }

  public void setHeatindexC(String heatindexC) {
    this.heatindexC = heatindexC;
  }

  public String getHumidexC() {
    return humidexC;
  }

  public void setHumidexC(String humidexC) {
    this.humidexC = humidexC;
  }

  public String getInDewpointC() {
    return inDewpointC;
  }

  public void setInDewpointC(String inDewpointC) {
    this.inDewpointC = inDewpointC;
  }

  public String getRainRateCmPerHour() {
    return rainRateCmPerHour;
  }

  public void setRainRateCmPerHour(String rainRateCmPerHour) {
    this.rainRateCmPerHour = rainRateCmPerHour;
  }

  public String getWindchillC() {
    return windchillC;
  }

  public void setWindchillC(String windchillC) {
    this.windchillC = windchillC;
  }

  public String getWindrunKm() {
    return windrunKm;
  }

  public void setWindrunKm(String windrunKm) {
    this.windrunKm = windrunKm;
  }

  public String getIntervalMinute() {
    return intervalMinute;
  }

  public void setIntervalMinute(String intervalMinute) {
    this.intervalMinute = intervalMinute;
  }

  public String getHourRainCm() {
    return hourRainCm;
  }

  public void setHourRainCm(String hourRainCm) {
    this.hourRainCm = hourRainCm;
  }

  public String getRain24Cm() {
    return rain24Cm;
  }

  public void setRain24Cm(String rain24Cm) {
    this.rain24Cm = rain24Cm;
  }

  public String getDayRainCm() {
    return dayRainCm;
  }

  public void setDayRainCm(String dayRainCm) {
    this.dayRainCm = dayRainCm;
  }

  public String getUsUnits() {
    return usUnits;
  }

  public void setUsUnits(String usUnits) {
    this.usUnits = usUnits;
  }

  public String getLuminosityLux() {
    return luminosityLux;
  }

  public void setLuminosityLux(String luminosityLux) {
    this.luminosityLux = luminosityLux;
  }

  public String getUvradiation() {
    return uvradiation;
  }

  public void setUvradiation(String uvradiation) {
    this.uvradiation = uvradiation;
  }

  public String getUv() {
    return uv;
  }

  public void setUv(String uv) {
    this.uv = uv;
  }

  public String getRainCm() {
    return rainCm;
  }

  public void setRainCm(String rainCm) {
    this.rainCm = rainCm;
  }

  public String getStormRainCm() {
    return stormRainCm;
  }

  public void setStormRainCm(String stormRainCm) {
    this.stormRainCm = stormRainCm;
  }

  public String getWeekRainCm() {
    return weekRainCm;
  }

  public void setWeekRainCm(String weekRainCm) {
    this.weekRainCm = weekRainCm;
  }

  public String getMonthRainCm() {
    return monthRainCm;
  }

  public void setMonthRainCm(String monthRainCm) {
    this.monthRainCm = monthRainCm;
  }

  public String getYearRainCm() {
    return yearRainCm;
  }

  public void setYearRainCm(String yearRainCm) {
    this.yearRainCm = yearRainCm;
  }

  public String getWindSpeedKmh() {
    return windSpeedKmh;
  }

  public void setWindSpeedKmh(String windSpeedKmh) {
    this.windSpeedKmh = windSpeedKmh;
  }

  public String getWindDir() {
    return windDir;
  }

  public void setWindDir(String windDir) {
    this.windDir = windDir;
  }

  public String getWindGustKmh() {
    return windGustKmh;
  }

  public void setWindGustKmh(String windGustKmh) {
    this.windGustKmh = windGustKmh;
  }

  public String getWindGustDir() {
    return windGustDir;
  }

  public void setWindGustDir(String windGustDir) {
    this.windGustDir = windGustDir;
  }

  public String getDaymaxwind() {
    return daymaxwind;
  }

  public void setDaymaxwind(String daymaxwind) {
    this.daymaxwind = daymaxwind;
  }

  public String getWh65Signal() {
    return wh65Signal;
  }

  public void setWh65Signal(String wh65Signal) {
    this.wh65Signal = wh65Signal;
  }

  public String getExtraTemp2C() {
    return extraTemp2C;
  }

  public void setExtraTemp2C(String extraTemp2C) {
    this.extraTemp2C = extraTemp2C;
  }

  public String getExtraHumid2() {
    return extraHumid2;
  }

  public void setExtraHumid2(String extraHumid2) {
    this.extraHumid2 = extraHumid2;
  }

  public String getWh31Ch2Battery() {
    return wh31Ch2Battery;
  }

  public void setWh31Ch2Battery(String wh31Ch2Battery) {
    this.wh31Ch2Battery = wh31Ch2Battery;
  }

  public String getWh31Ch2Signal() {
    return wh31Ch2Signal;
  }

  public void setWh31Ch2Signal(String wh31Ch2Signal) {
    this.wh31Ch2Signal = wh31Ch2Signal;
  }

  public String getWh40Battery() {
    return wh40Battery;
  }

  public void setWh40Battery(String wh40Battery) {
    this.wh40Battery = wh40Battery;
  }

  public String getWh40Signal() {
    return wh40Signal;
  }

  public void setWh40Signal(String wh40Signal) {
    this.wh40Signal = wh40Signal;
  }

  public String getWh68Battery() {
    return wh68Battery;
  }

  public void setWh68Battery(String wh68Battery) {
    this.wh68Battery = wh68Battery;
  }

  public String getWh68Signal() {
    return wh68Signal;
  }

  public void setWh68Signal(String wh68Signal) {
    this.wh68Signal = wh68Signal;
  }

  public String getExtraTemp3C() {
    return extraTemp3C;
  }

  public void setExtraTemp3C(String extraTemp3C) {
    this.extraTemp3C = extraTemp3C;
  }

  public String getExtraTemp4C() {
    return extraTemp4C;
  }

  public void setExtraTemp4C(String extraTemp4C) {
    this.extraTemp4C = extraTemp4C;
  }

  public String getExtraTemp5C() {
    return extraTemp5C;
  }

  public void setExtraTemp5C(String extraTemp5C) {
    this.extraTemp5C = extraTemp5C;
  }

  public String getExtraHumid3() {
    return extraHumid3;
  }

  public void setExtraHumid3(String extraHumid3) {
    this.extraHumid3 = extraHumid3;
  }

  public String getExtraHumid4() {
    return extraHumid4;
  }

  public void setExtraHumid4(String extraHumid4) {
    this.extraHumid4 = extraHumid4;
  }

  public String getExtraHumid5() {
    return extraHumid5;
  }

  public void setExtraHumid5(String extraHumid5) {
    this.extraHumid5 = extraHumid5;
  }

  public String getLightingDistanceKm() {
    return lightingDistanceKm;
  }

  public void setLightingDistanceKm(String lightingDistanceKm) {
    this.lightingDistanceKm = lightingDistanceKm;
  }

  public String getLightingStrikeCount() {
    return lightingStrikeCount;
  }

  public void setLightingStrikeCount(String lightingStrikeCount) {
    this.lightingStrikeCount = lightingStrikeCount;
  }

  public String getLastLightningDateTime() {
    return lastLightningDateTime;
  }

  public void setLastLightningDateTime(String lastLightningDateTime) {
    this.lastLightningDateTime = lastLightningDateTime;
  }
}
