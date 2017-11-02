package com.prosper.poc.restlogging;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class JSONRedactorTest {

	protected String json;
	protected String json_compact;
	protected String json_7_entries;
	protected String json_7_entries__compact;
	protected String json_70_entries__compact;
	
	@Before
	public void setUp() throws Exception {
		json = new String(Files.readAllBytes(Paths.get("./src/test/resources/testData.json")));
		json_compact = new String(Files.readAllBytes(Paths.get("./src/test/resources/testData_compact.json")));
		json_7_entries = new String(Files.readAllBytes(Paths.get("./src/test/resources/testData_7_entries.json")));
		json_7_entries__compact = new String(Files.readAllBytes(Paths.get("./src/test/resources/testData_7_entries_compact.json")));
		json_70_entries__compact = new String(Files.readAllBytes(Paths.get("./src/test/resources/testData_70_entries_compact.json")));
	}
	
	@Test
	public void testRedactPropStartWithUnderscore() {
		String redactedJson = JSONRedactor.redactPropsStartWithUnderscore(json);
		System.out.println(redactedJson);
	}
	
	@Test
	public void testRedactPropStartWithUnderscore_InCompactJSON() {
		String redactedJson = JSONRedactor.redactPropsStartWithUnderscore(json_compact);
		System.out.println(redactedJson);
	}
	
	@Test
	public void testRedactPropStartWithUnderscore_LargeJSON() {
		String redactedJson = JSONRedactor.redactPropsStartWithUnderscore(json_7_entries);
		assertThat(redactedJson).isNotNull();
		validateRedactedJSON(redactedJson);
	}
	
	
	@Test
	public void testRedactPropStartWithUnderscore_LargeCompactJSON() {
		String redactedJson = JSONRedactor.redactPropsStartWithUnderscore(json_7_entries__compact);
		assertThat(redactedJson).isNotNull();
		validateRedactedJSON(redactedJson);
	}
	
	@Test
	public void testRedactPropStartWithUnderscore_70EntriesCompactJSON() {
		String redactedJson = JSONRedactor.redactPropsStartWithUnderscore(json_70_entries__compact);
		assertThat(redactedJson).isNotNull();
		validateRedactedJSON(redactedJson);
	}
	protected void validateRedactedJSON(String redactedJson) {
		
		// Make sure there are as many "guid" as there are "_ssn":"_redacted"
		int numOfGUID = redactedJson.split("\"guid\"", -1).length-1;
		String redactedSSN = "\"_ssn\"\\s*:\\s*"+JSONRedactor.REDACTED_STRING_VALUE;
		int numOfSSN = redactedJson.split(redactedSSN, -1).length-1;
		assertThat(numOfSSN).isEqualTo(numOfGUID);
	}
}
