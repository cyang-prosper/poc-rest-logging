package com.prosper.poc.restlogging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONRedactor {
	
	private static Logger logger = LoggerFactory.getLogger(JSONRedactor.class);
	
	public static final String REDACTED_NUMBER_VALUE = "_redacted_";
	public static final String REDACTED_STRING_VALUE = "\"_redacted_\"";
	
	// A numeric JSON property e.g. "_amount": 2e10
	private static final Pattern NUMERIC_PROP_PATTERN = Pattern.compile("(\"_[^\"]+\")(\\s*:\\s*)([\\deE\\-\\.\\+]+)([\\s|,|}|\n]?)");
	
	// A string property e.g. "_secret message": "something",
	private static final Pattern STRING_PROP_PATTERN = Pattern.compile("(\"_[^\"]+\")(\\s*:\\s*)(\"[^\"]*\")([\\s|,|}|\n]?)");
    
	
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
		
		// Convert "_propName" : 123 to "_propName" : (redacted)
        
        Matcher matcher = NUMERIC_PROP_PATTERN.matcher(json);
        int offset = 0;
        while(matcher.find()) {
        		json = json.substring(0, matcher.start()+offset)+
        				matcher.group(1)+
        				matcher.group(2)+
        				REDACTED_NUMBER_VALUE+
        				matcher.group(4)+
        				json.substring(matcher.end()+offset);
        		offset += REDACTED_NUMBER_VALUE.length()-matcher.group(3).length();
        }
        
        // Convert "_propName" : " something " to "_propName" : "(redacted)"
        matcher = STRING_PROP_PATTERN.matcher(json);
        offset = 0;
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
