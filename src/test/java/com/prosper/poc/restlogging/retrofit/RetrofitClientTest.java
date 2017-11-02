package com.prosper.poc.restlogging.retrofit;

import org.junit.Test;

public class RetrofitClientTest {

	protected RetrofitClient client = new RetrofitClient();
	
	@Test
	public void testLoggingBody() {
		client.setRequest();
	}
	
}
