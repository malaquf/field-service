package com.example.fieldservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.fieldservice.model.Field;

@Repository
public interface FieldRepository extends MongoRepository<Field, String> {
	
}
