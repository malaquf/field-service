package com.example.fieldservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "fieldservice")
@Data
public class ConfigProperties {

	@NestedConfigurationProperty
	private FieldMonitoringConfig monitoring;
}
