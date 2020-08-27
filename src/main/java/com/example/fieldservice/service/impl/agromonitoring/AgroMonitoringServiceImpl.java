package com.example.fieldservice.service.impl.agromonitoring;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.fieldservice.config.ConfigProperties;
import com.example.fieldservice.config.FieldMonitoringConfig;
import com.example.fieldservice.config.FieldMonitoringEndpointsConfig;
import com.example.fieldservice.dto.WeatherDTO;
import com.example.fieldservice.dto.WeatherHistoryDTO;
import com.example.fieldservice.model.Field;
import com.example.fieldservice.service.MonitoringService;
import com.example.fieldservice.service.impl.agromonitoring.dto.MainWeatherEntry;
import com.example.fieldservice.service.impl.agromonitoring.dto.Polygon;
import com.example.fieldservice.service.impl.agromonitoring.dto.WeatherHistoryEntry;

@Service
public class AgroMonitoringServiceImpl implements MonitoringService {

	private static final Logger log = LoggerFactory.getLogger(AgroMonitoringServiceImpl.class);
	
	@Autowired
	private ConfigProperties configProperties;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public final String create(final Field field) {
		log.debug("Creating field {}", field);
		String id = null;
		FieldMonitoringConfig monitoringConfig = this.configProperties.getMonitoring();
		String appId = monitoringConfig.getAppId();
		FieldMonitoringEndpointsConfig endpoints = monitoringConfig.getEndpoints();
		String endpoint = endpoints.getCreatePolygon();
		
		URI uri = UriComponentsBuilder.
				fromUriString(endpoint)
				.queryParam("appid", appId)
				.build()
				.toUri();
		
		Polygon polygon = Polygon.builder()
				.name(field.getName())
				.geoJson(field.getBounderies().getGeoJson())
				.build();

		ResponseEntity<Polygon> responseEntity = this.restTemplate.postForEntity(uri, polygon, Polygon.class);
		HttpStatus statusCode = responseEntity.getStatusCode();
		if (HttpStatus.OK.equals(statusCode) || HttpStatus.CREATED.equals(statusCode)) {
			polygon = responseEntity.getBody();
			id = polygon == null ? null : polygon.getId();
			field.setId(id);
			log.debug("Created field {}", field);
		}
		else {
			log.error("Failed to create field with response {}", responseEntity);
		}
		return id;
	}

	@Override
	public final WeatherHistoryDTO getWeatherHistory(final Field field) {
		WeatherHistoryDTO result = null;
		FieldMonitoringConfig monitoringConfig = this.configProperties.getMonitoring();
		String appId = monitoringConfig.getAppId();
		FieldMonitoringEndpointsConfig endpoints = monitoringConfig.getEndpoints();
		String endpoint = endpoints.getWeatherHistory();
		
		long start = LocalDate.now().toEpochDay();
		long end = LocalDate.now().minusDays(7).toEpochDay();
		
		URI uri = UriComponentsBuilder.
				fromUriString(endpoint)
				.queryParam("appid", appId)
				.queryParam("polyid", field.getId())
				.queryParam("start", start)
				.queryParam("end", end)
				.build()
				.toUri();

		ResponseEntity<WeatherHistoryEntry[]> responseEntity = this.restTemplate.getForEntity(uri, WeatherHistoryEntry[].class);
		if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
			WeatherHistoryEntry[] weatherHistoryEntries = responseEntity.getBody();
			result = convert(weatherHistoryEntries);
		}
		
		return result;
	}
	
	private WeatherHistoryDTO convert(WeatherHistoryEntry[] weatherHistoryEntries) {
		List<WeatherDTO> weatherEntryDTOs = new ArrayList<WeatherDTO>();
		for (WeatherHistoryEntry weatherHistoryEntry : weatherHistoryEntries) {
			MainWeatherEntry mainWeatherEntry = weatherHistoryEntry.getMain();
			WeatherDTO weatherEntryDTO = WeatherDTO.builder()
				.humidity(mainWeatherEntry.getHumidity())
				.temperature(mainWeatherEntry.getTemp())
				.temperatureMax(mainWeatherEntry.getTempMax())
				.temperatureMin(mainWeatherEntry.getTempMin())
				.timestamp(weatherHistoryEntry.getDt())
				.build();
			weatherEntryDTOs.add(weatherEntryDTO);
		}
		return WeatherHistoryDTO.builder().weather(weatherEntryDTOs).build();
	}

	@Override
	public void update(Field field) {
		FieldMonitoringConfig monitoringConfig = this.configProperties.getMonitoring();
		String appId = monitoringConfig.getAppId();
		FieldMonitoringEndpointsConfig endpoints = monitoringConfig.getEndpoints();
		String endpoint = endpoints.getUpdatePolygon();
		
		URI uri = UriComponentsBuilder.
				fromUriString(endpoint + field.getId())
				.queryParam("appid", appId)
				.build()
				.toUri();

		Polygon polygon = Polygon.builder()
			.geoJson(field.getBounderies().getGeoJson())
			.id(field.getId())
			.name(field.getName())
			.build();
		
		this.restTemplate.put(uri, polygon);
	}

	@Override
	public void delete(String id) {
		FieldMonitoringConfig monitoringConfig = this.configProperties.getMonitoring();
		String appId = monitoringConfig.getAppId();
		FieldMonitoringEndpointsConfig endpoints = monitoringConfig.getEndpoints();
		String endpoint = endpoints.getDeletePolygon();
		
		URI uri = UriComponentsBuilder.
				fromUriString(endpoint + id)
				.queryParam("appid", appId)
				.build()
				.toUri();

		this.restTemplate.delete(uri);
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}
