package com.example.fieldservice.controller;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.fieldservice.dto.WeatherHistoryDTO;
import com.example.fieldservice.model.Field;
import com.example.fieldservice.service.FieldService;
import com.example.fieldservice.service.MonitoringService;

@Controller
public class FieldController {
	
	private static final Logger log = LoggerFactory.getLogger(FieldController.class);
	
	@Autowired
	private FieldService fieldService;
	
	@Autowired
	private MonitoringService monitoringService;
	
	@PostMapping(value = "/fields")
	public final ResponseEntity<Void> createField(@RequestBody Field field) {
		log.debug("Creating field {}", field);
		String id = this.monitoringService.create(field);
		this.fieldService.save(field);
        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
		URI uri = ucBuilder.path("/fields/{id}").buildAndExpand(id).toUri();
		log.debug("Field created created: {}", uri);
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/fields/{id}")
	public final ResponseEntity<Void> updateField(@PathVariable String id, @RequestBody Field field) {
		log.debug("Updating field {}", field);
		ResponseEntity<Void> response = null;
		if (!this.fieldService.exists(id)) {
			log.debug("Field with id {} not found", id);
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
		else {
			this.monitoringService.update(field);
			this.fieldService.save(field);
			log.debug("Field with id {} updated: {}", id, field);
			response = new ResponseEntity<>(HttpStatus.OK);
		}
		return response;
	}
	
	@GetMapping(value = "/fields/{id}")
	public final ResponseEntity<Field> getField(@PathVariable final String id) {
		log.debug("Retrieving field with id {}", id);
		Optional<Field> field = fieldService.findById(id);
		boolean fieldFound = field.isPresent();
		log.debug("Field found: {}, {}", fieldFound, field.orElse(null));
        return fieldFound ? 
        		new ResponseEntity<>(field.get(), HttpStatus.OK) : 
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value = "/fields/{id}/weather")
	public final ResponseEntity<WeatherHistoryDTO> getWeatherHistory(@PathVariable final String id) {
		log.debug("Retrieving weather history for field with id {}", id);
		ResponseEntity<WeatherHistoryDTO> response = null;
		Optional<Field> field = fieldService.findById(id);
		if (field.isPresent()) {
			WeatherHistoryDTO weatherHistory = this.monitoringService.getWeatherHistory(field.get());
			response = new ResponseEntity<>(weatherHistory, HttpStatus.OK);
		}
		else {
			log.debug("Field with id {} not found", id);
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        return response;
                
	}
	
	@DeleteMapping(value = "/fields/{id}")
	public final ResponseEntity<Void> deleteField(@PathVariable final String id) {
		log.debug("Deleting field with id {}", id);
		ResponseEntity<Void> response = null;
		if (!fieldService.exists(id)) {
			log.debug("Field with id {} not found", id);
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
		else {
			this.monitoringService.delete(id);
			this.fieldService.delete(id);
			log.debug("Field with id {} deleted", id);
			response = new ResponseEntity<>(HttpStatus.OK);
		}
		return response;
	}
}
