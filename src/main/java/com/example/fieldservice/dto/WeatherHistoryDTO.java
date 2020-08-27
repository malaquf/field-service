package com.example.fieldservice.dto;

import java.util.List;

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
public class WeatherHistoryDTO {

	private List<WeatherDTO> weather;
}
