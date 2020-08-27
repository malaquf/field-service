package com.example.fieldservice.service.impl.agromonitoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainWeatherEntry {

	private Double temp;
	private Integer pressure;
	private Integer humidity;
	@JsonProperty("temp_min")
	private Double tempMin;
	@JsonProperty("temp_max")
	private Double tempMax;
}
