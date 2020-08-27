package com.example.fieldservice.service.impl.agromonitoring.dto;

import org.geojson.GeoJsonObject;

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
public class Polygon {

	private String id;
	private String name;
	@JsonProperty("geo_json")
	private GeoJsonObject geoJson;
}
