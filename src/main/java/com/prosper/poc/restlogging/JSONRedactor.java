package com.prosper.poc.restlogging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONRedactor {
	
	private static Logger logger = LoggerFactory.getLogger(JSONRedactor.class);
	
	public static final String REDACTED_NUMERIC_VALUE = "_redacted_";
	public static final String REDACTED_STRING_VALUE = "\"_redacted_\"";
	
	// A numeric value in JSON e.g. -2.12e10
	private static final String NUMERIC_VALUE_PATTERN = "([\\deE\\-\\.\\+]+)([\\s|,|}|\n]?)";
	
	// A string value in JSON (with no \" in the middle) e.g. "abc 123."
	private static final String STRING_VALUE_PATTERN = "(\"[^\"]*\")([\\s|,|}|\n]?)";
	
	// A numeric JSON property e.g. "_amount": 2e10
	private static final Pattern NUMERIC_PROP_STARTS_WITH_UNDERSCORE_PATTERN = Pattern.compile("(\"_[^\"]+\")(\\s*:\\s*)"+NUMERIC_VALUE_PATTERN);
	
	// A string property e.g. "_secret message": "something",
	private static final Pattern STRING_PROP_STARTS_WITH_UNDERSCORE_PATTERN = Pattern.compile("(\"_[^\"]+\")(\\s*:\\s*)"+STRING_VALUE_PATTERN);
    
	// A property name template e.g. "{PROP_NAME}":
	// Just replace {PROP_NAME} with an actual property name
	private static final String REGEX_PROP_NAME_CONTAINING = "(\"[^\"]*{PROP_NAME}[^\"]*\")(\\s*:\\s*)";
	private static final String REGEX_PROP_NAME_STARTS_WITH = "(\"{PROP_NAME}[^\"]*\")(\\s*:\\s*)";
	private static final String REGEX_PROP_NAME_ENDS_WITH = "(\"[^\"]*{PROP_NAME}\")(\\s*:\\s*)";
	
	
	/**
	 * Redact all properties with name comtaining a certain phrase
	 * 
	 * @param json
	 * @param propName
	 * @return
	 */
	public static String redactPropsWithNameContaining(String json, String propName) {
		if(json == null || propName == null || propName.trim().length() == 0) {
			return json;
		}

		String regex = REGEX_PROP_NAME_CONTAINING.replace("{PROP_NAME}", propName);
		return redactPropsWithName(json, regex);
	}
	
	
	/**
	 * Redact all properties with name starts with a certain phrase
	 * 
	 * @param json
	 * @param propName
	 * @return
	 */
	public static String redactPropsWithNameStartsWith(String json, String propName) {
		if(json == null || propName == null || propName.trim().length() == 0) {
			return json;
		}

		String regex = REGEX_PROP_NAME_STARTS_WITH.replace("{PROP_NAME}", propName);
		return redactPropsWithName(json, regex);
	}

	
	/**
	 * Redact all properties with name starts with a certain phrase
	 * 
	 * @param json
	 * @param propName
	 * @return
	 */
	public static String redactPropsWithNameEndsWith(String json, String propName) {
		if(json == null || propName == null || propName.trim().length() == 0) {
			return json;
		}

		String regex = REGEX_PROP_NAME_ENDS_WITH.replace("{PROP_NAME}", propName);
		return redactPropsWithName(json, regex);
	}
	

	/**
	 * Redact all properties with name matching the regex
	 * 
	 * @param json
	 * @param regex
	 * @return
	 */
	private static String redactPropsWithName(String json, String regex) {
		Pattern numericPropPattern = Pattern.compile(regex+NUMERIC_VALUE_PATTERN);
		Pattern stringPropPattern = Pattern.compile(regex+STRING_VALUE_PATTERN);
		
		json = redactNumericValue(json, numericPropPattern);
		json = redactStringValue(json, stringPropPattern);
        
        return json;
	}
	
	
	/**
	 * Redact the values of a property whose name starts with an underscore.
	 * Note that this method does NOT redact:
	 * <ul>
	 *  <li>a boolean, </li>
	 *  <li>a complex object, </li>
	 *  <li>an array, </li>
	 *  <li>a string literal with \" in it</li>
	 * </ul>
	 * @param json
	 * @return
	 */
	public static String redactPropsStartWithUnderscore(String json) {
		if(json==null) {
			return null;
		}
		
        json = redactNumericValue(json, NUMERIC_PROP_STARTS_WITH_UNDERSCORE_PATTERN);
		json = redactStringValue(json, STRING_PROP_STARTS_WITH_UNDERSCORE_PATTERN);
        
        return json;
	}
	
	/**
	 * Redact a numeric value while maintaining the prop name
	 * e.g. Convert "_propName" : 123 to "_propName" : (redacted)
	 * 
	 * @param json
	 * @param pattern
	 * @return
	 */
	private static String redactNumericValue(String json, Pattern pattern) {
		Matcher matcher = pattern.matcher(json);
        int offset = 0;
        while(matcher.find()) {
        		json = json.substring(0, matcher.start()+offset)+
        				matcher.group(1)+
        				matcher.group(2)+
        				REDACTED_NUMERIC_VALUE+
        				matcher.group(4)+
        				json.substring(matcher.end()+offset);
        		offset += REDACTED_NUMERIC_VALUE.length()-matcher.group(3).length();
        }
        return json;
	}
	
	/**
	 * Redact a string value while maintaining the prop name
	 * e.g. Convert "_propName" : " something " to "_propName" : "(redacted)"
	 * 
	 * @param json
	 * @param pattern
	 * @return
	 */
	private static String redactStringValue(String json, Pattern pattern) {
		Matcher matcher = pattern.matcher(json);
        int offset = 0;
        while(matcher.find()) {
        		json = json.substring(0, matcher.start()+offset)+
        				matcher.group(1)+
        				matcher.group(2)+
        				REDACTED_STRING_VALUE+
        				matcher.group(4)+
        				json.substring(matcher.end()+offset);
        		offset += REDACTED_STRING_VALUE.length()-matcher.group(3).length();
        }
        return json;
	}
	
}
