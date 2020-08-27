package com.example.fieldservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.example.fieldservice.config.ConfigProperties;
import com.example.fieldservice.config.FieldMonitoringConfig;
import com.example.fieldservice.config.MongoRepositoryConfiguration;
import com.example.fieldservice.dto.WeatherHistoryDTO;
import com.example.fieldservice.model.Field;
import com.example.fieldservice.utils.FileUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(MongoRepositoryConfiguration.class)
@ActiveProfiles("it")
class FieldServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ConfigProperties configProperties;
	
	private MockRestServiceServer mockServer;
	
	private FieldMonitoringConfig monitoringConfig;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.monitoringConfig = this.configProperties.getMonitoring();
		this.mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	private void mockThirdPartyServices() throws Exception {
		File file = FileUtils.getResourceFile("responses/createPolygon.json");
		String expectedCreatePolygonStringResponse = new String(Files.readAllBytes(file.toPath()));
		this.mockServer.expect(requestTo("http://api.agromonitoring.com/agro/1.0/polygons?appid="+this.monitoringConfig.getAppId()))
		.andExpect(method(HttpMethod.POST))
		.andRespond(withSuccess(expectedCreatePolygonStringResponse, MediaType.APPLICATION_JSON));
		
		this.mockServer.expect(requestTo("http://api.agromonitoring.com/agro/1.0/polygons/5f46aa28714b521409e0f8e9?appid="+this.monitoringConfig.getAppId()))
		.andExpect(method(HttpMethod.PUT))
		.andRespond(withSuccess());

		file = FileUtils.getResourceFile("responses/getWeatherHistory.json");
		String expectedWeatherHistoryStringResponse = new String(Files.readAllBytes(file.toPath()));
		this.mockServer.expect(requestTo("http://api.agromonitoring.com/agro/1.0/weather/history?appid=c73efcd83c3f3d3c70d7de1edcc8dce8&polyid=5f46aa28714b521409e0f8e9&start=18501&end=18494"))
		.andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(expectedWeatherHistoryStringResponse, MediaType.APPLICATION_JSON));
		
		this.mockServer.expect(requestTo("http://api.agromonitoring.com/agro/1.0/polygons/5f46aa28714b521409e0f8e9?appid="+this.monitoringConfig.getAppId()))
		.andExpect(method(HttpMethod.DELETE))
		.andRespond(withSuccess());
	}
	
	@Test
	void integrationTest() throws Exception {
		mockThirdPartyServices();
		
		// UC1: create field
		Field field = FileUtils.readJson("requests/createField.json", Field.class);
		ResponseEntity<Void> createFieldResponse = testRestTemplate.postForEntity(createURLWithPort("/fields"), field, Void.class);
		assertEquals(HttpStatus.CREATED, createFieldResponse.getStatusCode());
		String newFieldURI = createFieldResponse.getHeaders().getLocation().getPath();
		assertTrue(newFieldURI.startsWith("/fields/"));
		
		// UC2: get field
		ResponseEntity<Field> getFieldReponse = this.testRestTemplate.getForEntity(createURLWithPort(newFieldURI), Field.class);
		assertEquals(HttpStatus.OK, getFieldReponse.getStatusCode());
		assertThat(getFieldReponse.getBody())
			.usingRecursiveComparison()
			.ignoringFieldsMatchingRegexes(".*updated", ".*created", ".*bounderies")
			.isEqualTo(field);
		
		// UC3: update field
		Field expectedUpdatedField = FileUtils.readJson("requests/updateField.json", Field.class);
		//this.testRestTemplate.put(createURLWithPort(newFieldURI), expectedUpdatedField);
		ResponseEntity<Void> updateResponse = this.testRestTemplate.exchange(
				createURLWithPort(newFieldURI), 
				HttpMethod.PUT, new HttpEntity<>(expectedUpdatedField), Void.class);
		assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
		
		getFieldReponse = testRestTemplate.getForEntity(createURLWithPort(newFieldURI), Field.class);
		assertEquals(HttpStatus.OK, getFieldReponse.getStatusCode());
		assertThat(getFieldReponse.getBody())
			.usingRecursiveComparison()
			.ignoringFieldsMatchingRegexes("updated", "created", "bounderies")
			.isEqualTo(expectedUpdatedField);
		
		// UC4: get field weather history
		WeatherHistoryDTO expectedHistory = FileUtils.readJson("responses/expectedHistory.json", WeatherHistoryDTO.class);
		ResponseEntity<WeatherHistoryDTO> getHistoryReponse = this.testRestTemplate.getForEntity(createURLWithPort(newFieldURI+"/weather"), WeatherHistoryDTO.class);
		assertEquals(HttpStatus.OK, getHistoryReponse.getStatusCode());
		assertThat(getHistoryReponse.getBody())
			.isEqualTo(expectedHistory);
		
		// UC5: delete field
		this.testRestTemplate.delete(createURLWithPort(newFieldURI));
		getFieldReponse = this.testRestTemplate.getForEntity(createURLWithPort(newFieldURI), Field.class);
		assertEquals(HttpStatus.NOT_FOUND, getFieldReponse.getStatusCode());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
