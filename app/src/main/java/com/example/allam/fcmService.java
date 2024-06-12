package com.example.allam;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface fcmService {
    @Headers("Content-Type: application/json")
    @POST("fcm/send")
    Call<notifikasiResponse> sendNotification(@Header("Authorization") String authHeader, @Body notifikasiRequest body);

}
