package com.example.fieldservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherEntryDTO {

	private Long timestamp;
	private Double temperature;
	private Integer humidity;
	private Double temperatureMax;
	private Double temperatureMin;
}
