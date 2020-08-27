package com.example.fieldservice.service;

import com.example.fieldservice.dto.WeatherHistoryDTO;
import com.example.fieldservice.model.Field;

public interface MonitoringService {

	public String create(Field field);
	
	public void update(Field field);
	
	public void delete(String id);
	
	public WeatherHistoryDTO getWeatherHistory(Field field);
}
