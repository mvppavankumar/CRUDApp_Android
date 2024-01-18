package com.example.android_curd_native;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("addBooks")
    Call<JSONObject> addBook(@Body Map<String, Object> params);
}
