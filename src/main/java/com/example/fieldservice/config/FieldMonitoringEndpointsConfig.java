package com.example.fieldservice.config;

import lombok.Data;

@Data
public class FieldMonitoringEndpointsConfig {

	private String createPolygon;
	private String updatePolygon;
	private String deletePolygon;
	private String weatherHistory;
}
