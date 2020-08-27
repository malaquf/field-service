package com.example.fieldservice.utils;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for reading json files.
 *
 */
public class FileUtils {
	
	private FileUtils() {
		//hidden constructor
	}
	
	/**
	 * Reads json from resources for given src name and class type.
	 * @param <T> Class type to be read from json.
	 * @param src src name.
	 * @param type Class type to be read from json.
	 * @return object read from json.
	 * @throws JsonParseException if an exception occurs during parsing.
	 * @throws JsonMappingException if an exception occurs during mapping.
	 * @throws IOException if an exception occurs during file read.
	 */
	public static <T> T readJson(String src, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
		File file = getResourceFile(src);
		ObjectMapper mapper = new ObjectMapper();
		T value = mapper.readValue(file, type);
		return value;
	}

	public static File getResourceFile(String resource) {
		ClassLoader classLoader = FileUtils.class.getClassLoader();
		return new File(classLoader.getResource(resource).getFile());
	}
}
