package com.example.fieldservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuration class for enabling JPA auditing.
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.example.fieldservice.repository")
public class MongoRepositoryConfiguration {

}
