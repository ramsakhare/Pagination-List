package com.SampleApp.Sample.network;


import com.SampleApp.Sample.helper.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    
    public static Retrofit retrofit;
    private static API apiRequests;

    // Singleton Instance of APIRequests
    public static API getInstance() {
        if (apiRequests == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.APP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiRequests = retrofit.create(API.class);
            
            return apiRequests;
            
        }
        else {
            return apiRequests;
        }
    }

}
