package com.prosper.poc.restlogging.retrofit;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class CustomExclusionStrategy implements ExclusionStrategy {
	 
//	private Class<?> classToExclude;
// 
//	public CustomExclusionStrategy(Class<?> classToExclude) {
//		this.classToExclude = classToExclude;
//	}
 
	// This method is called for all fields. if the method returns false the
	// field is excluded from serialization
	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		System.out.println(f.getName());
		return !f.getName().startsWith("subject");
	}
 
	// This method is called for all classes. If the method returns false the
	// class is excluded.
	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
//		if (clazz.equals(classToExclude))
//			return true;
		return false;
	}
 
}
