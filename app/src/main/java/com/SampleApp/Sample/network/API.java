package com.SampleApp.Sample.network;


import com.SampleApp.Sample.model.PageModel;
import com.SampleApp.Sample.model.UserDetailsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface API {

    @GET("api/users")
    Call<PageModel> getPageDetails(@Query("page") int page,
                                         @Query("per_page") int per_page

    );

}
