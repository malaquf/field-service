package com.example.fieldservice.config;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Data;

@Data
public class FieldMonitoringConfig {

	private String appId;
	@NestedConfigurationProperty
	private FieldMonitoringEndpointsConfig endpoints;
}
