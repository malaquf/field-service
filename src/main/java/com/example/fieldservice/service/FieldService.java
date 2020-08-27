package com.example.fieldservice.service;

import java.util.Optional;

import com.example.fieldservice.model.Field;

public interface FieldService {

	public boolean exists(String id);
	
	public void save(Field field);

    public void delete(String id);

    public Optional<Field> findById(String id);
    
}
