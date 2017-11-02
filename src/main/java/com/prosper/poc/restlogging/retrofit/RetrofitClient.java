package com.prosper.poc.restlogging.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

	HttpLoggingInterceptor logInterceptor;
	OkHttpClient.Builder client;
	
	public RetrofitClient() {
		client = new OkHttpClient.Builder();
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		client.addInterceptor(loggingInterceptor);
	}
	
	public void setRequest() {
		Retrofit retrofit = new Retrofit.Builder()
		        .baseUrl("http://api.learn2crack.com")
		        .client(client.build())
		        .addConverterFactory(GsonConverterFactory.create())
		        .build();
	}
}
