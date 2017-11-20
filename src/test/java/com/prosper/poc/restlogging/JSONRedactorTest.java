package com.prosper.poc.restlogging;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class JSONRedactorTest {
	
	// List of properties to redact and their types
	@SuppressWarnings("serial")
	private static final Map<String, Class<?>> propNamesExactMatch = new HashMap<String, Class<?>>() {{
		put("email", String.class);
		put("latitude", Number.class);
	}};
	
	
	@SuppressWarnings("serial")
	private static final Map<String, Class<?>> propNamesStartWith = new HashMap<String, Class<?>>() {{
		put("eye", String.class);
		put("lat", Number.class);
	}};
	
	
	protected String json_7_entries;
	protected String json_7_entries_compact;
	protected String json_70_entries_compact;
	
	
	@Before
	public void setUp() throws Exception {
		json_7_entries = new String(Files.readAllBytes(Paths.get("./src/test/resources/testData_7_entries.json")));
		json_7_entries_compact = new String(Files.readAllBytes(Paths.get("./src/test/resources/testData_7_entries_compact.json")));
		json_70_entries_compact = new String(Files.readAllBytes(Paths.get("./src/test/resources/testData_70_entries_compact.json")));
	}
	
	
	@Test
	public void testRedactPropMatchingName_7EntriesJSON() {
		String redactedJson = json_7_entries;
		
		for(String propName: propNamesExactMatch.keySet()) {
			redactedJson = JSONRedactor.redactPropsWithNameContaining(redactedJson, propName);
		}
		
		validateRedactPropMatchingName(redactedJson);
	}
	
	
	@Test
	public void testRedactPropMatchingName_7EntriesCompactJSON() {
		String redactedJson = json_7_entries_compact;
		
		for(String propName: propNamesExactMatch.keySet()) {
			redactedJson = JSONRedactor.redactPropsWithNameContaining(redactedJson, propName);
		}
		
		validateRedactPropMatchingName(redactedJson);
	}
	
	
	@Test
	public void testRedactPropMatchingName_70EntriesCompactJSON() {
		String redactedJson = json_70_entries_compact;
		
		for(String propName: propNamesExactMatch.keySet()) {
			redactedJson = JSONRedactor.redactPropsWithNameContaining(redactedJson, propName);
		}
		
		validateRedactPropMatchingName(redactedJson);
	}

	
	private void validateRedactPropMatchingName(String redactedJson) {
		// Verify the redacted results
		int numOfGUID = redactedJson.split("\"guid\"", -1).length-1;
		
		for(String propName: propNamesExactMatch.keySet()) {
			String redactedStringRegex = "\""+propName+"\"\\s*:\\s*"+ (propNamesExactMatch.get(propName) == String.class? JSONRedactor.REDACTED_STRING_VALUE : JSONRedactor.REDACTED_NUMERIC_VALUE);
			int numOfProp = redactedJson.split(redactedStringRegex, -1).length-1;
			assertThat(numOfProp).isEqualTo(numOfGUID);	
		}
	}
	

	@Test
	public void testRedactPropsStartWithName_7EntriesJSON() {
		String redactedJson = json_7_entries;
		
		for(String propName: propNamesStartWith.keySet()) {
			redactedJson = JSONRedactor.redactPropsWithNameStartsWith(redactedJson, propName);
		}
		
		validateRedactPropsStartWithName(redactedJson);
	}
	
	
	@Test
	public void testRedactPropsStartWithName_7EntriesCompactJSON() {
		String redactedJson = json_7_entries_compact;
		
		for(String propName: propNamesStartWith.keySet()) {
			redactedJson = JSONRedactor.redactPropsWithNameContaining(redactedJson, propName);
		}
		
		validateRedactPropsStartWithName(redactedJson);
	}
	
	
	@Test
	public void testRedactPropsStartWithName_70EntriesCompactJSON() {
		String redactedJson = json_70_entries_compact;
		
		for(String propName: propNamesStartWith.keySet()) {
			redactedJson = JSONRedactor.redactPropsWithNameContaining(redactedJson, propName);
		}
		
		validateRedactPropsStartWithName(redactedJson);
	}

	
	private void validateRedactPropsStartWithName(String redactedJson) {
		// Verify the redacted results
		int numOfGUID = redactedJson.split("\"guid\"", -1).length-1;
		
		for(String propName: propNamesStartWith.keySet()) {
			String redactedStringRegex = "\""+propName+"[^\"]*\"\\s*:\\s*"+ (propNamesStartWith.get(propName) == String.class? JSONRedactor.REDACTED_STRING_VALUE : JSONRedactor.REDACTED_NUMERIC_VALUE);
			int numOfProp = redactedJson.split(redactedStringRegex, -1).length-1;
			assertThat(numOfProp).isEqualTo(numOfGUID);	
		}
	}
	
	
	private void validateRedactPropsExactMatch(String redactedJson) {
		// Verify the redacted results
		int numOfGUID = redactedJson.split("\"guid\"", -1).length-1;
		
		for(String propName: propNamesExactMatch.keySet()) {
			String redactedStringRegex = "\""+propName+"[^\"]*\"\\s*:\\s*"+ (propNamesExactMatch.get(propName) == String.class? JSONRedactor.REDACTED_STRING_VALUE : JSONRedactor.REDACTED_NUMERIC_VALUE);
			int numOfProp = redactedJson.split(redactedStringRegex, -1).length-1;
			assertThat(numOfProp).isEqualTo(numOfGUID);	
		}
	}

	@Test
	public void testRedactPropsStartWithUnderscore_7EntriesJSON() {
		String redactedJson = JSONRedactor.redactPropsStartWithUnderscore(json_7_entries);
		assertThat(redactedJson).isNotNull();
		validateRedactedPropsStartWithUnderscore(redactedJson);
	}
	
	@Test
	public void testRedactPropsStartWithUnderscore_7EntriesCompactJSON() {
		String redactedJson = JSONRedactor.redactPropsStartWithUnderscore(json_7_entries_compact);
		assertThat(redactedJson).isNotNull();
		validateRedactedPropsStartWithUnderscore(redactedJson);
	}
	
	@Test
	public void testRedactPropsStartWithUnderscore_70EntriesCompactJSON() {
		String redactedJson = JSONRedactor.redactPropsStartWithUnderscore(json_70_entries_compact);
		assertThat(redactedJson).isNotNull();
		validateRedactedPropsStartWithUnderscore(redactedJson);
	}
	
	
	protected void validateRedactedPropsStartWithUnderscore(String redactedJson) {
		int numOfGUID = redactedJson.split("\"guid\"", -1).length-1;
		
		// Make sure there are as many "guid" as there are "_ssn":"_redacted_"
		String redactedSSN = "\"_ssn\"\\s*:\\s*"+JSONRedactor.REDACTED_STRING_VALUE;
		int numOfSSN = redactedJson.split(redactedSSN, -1).length-1;
		assertThat(numOfSSN).isEqualTo(numOfGUID);
		
		// Make sure there are as many "guid" as there are "_age":"_redacted"
		String redactedAge = "\"_age\"\\s*:\\s*"+JSONRedactor.REDACTED_NUMERIC_VALUE;
		int numOfAge = redactedJson.split(redactedAge, -1).length-1;
		assertThat(numOfAge).isEqualTo(numOfGUID);
	}
	
}
