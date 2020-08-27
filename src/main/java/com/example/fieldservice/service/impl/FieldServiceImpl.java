package com.example.fieldservice.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fieldservice.model.Field;
import com.example.fieldservice.repository.FieldRepository;
import com.example.fieldservice.service.FieldService;
import com.example.fieldservice.service.MonitoringService;

@Service
public class FieldServiceImpl implements FieldService {

	@Autowired
	private FieldRepository fieldRepository;
	
	@Autowired MonitoringService monitoringService;
	
	@Override
	public final boolean exists(final String id) {
		return this.fieldRepository.existsById(id);
	}

	@Override
	public final void save(final Field field) {
		this.fieldRepository.save(field);
	}

	@Override
	public final void delete(final String id) {
		this.fieldRepository.deleteById(id);
	}

	@Override
	public final Optional<Field> findById(final String id) {
		return this.fieldRepository.findById(id);
	}

}
