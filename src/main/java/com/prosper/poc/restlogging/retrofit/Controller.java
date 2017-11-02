package com.prosper.poc.restlogging.retrofit;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller implements Callback<List<Change>> {

	static final String BASE_URL = "https://git.eclipse.org/r/";

	public void start() {
		OkHttpClient.Builder client = new OkHttpClient.Builder();
		CustomHttpLoggingInterceptor loggingInterceptor = new CustomHttpLoggingInterceptor();
		loggingInterceptor.setLevel(CustomHttpLoggingInterceptor.Level.BODY);
		client.addInterceptor(loggingInterceptor);
		
		Gson gson = new GsonBuilder()
				.setLenient()
				.setExclusionStrategies(new CustomExclusionStrategy())
				.create();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BASE_URL)
				.client(client.build())
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		GerritAPI gerritAPI = retrofit.create(GerritAPI.class);

		Call<List<Change>> call = gerritAPI.loadChanges("status:open");
		call.enqueue(this);

	}

	@Override
	public void onResponse(Call<List<Change>> call, Response<List<Change>> response) {
		if(response.isSuccessful()) {
			List<Change> changesList = response.body();
			//changesList.forEach(change -> System.out.println(change.subject));
		} else {
			//System.out.println(response.errorBody());
		}
	}

	@Override
	public void onFailure(Call<List<Change>> call, Throwable t) {
		t.printStackTrace();
		throw new RuntimeException(t);
	}
	
	public static void main(String[] args) {
        Controller controller = new Controller();
        controller.start();
    }
}
