package com.example.allam;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fcmClient {
    private static final String BASE_URL = "https://fcm.googleapis.com/";

    private fcmService apiService;

    public fcmClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(fcmService.class);
    }
    public void sendNotification(String to, String title, String body) {
        notifikasiRequest.Notification notification = new notifikasiRequest.Notification(title, body);
        notifikasiRequest request = new notifikasiRequest(to, notification);

        apiService.sendNotification(request).enqueue(new Callback<notifikasiResponse>() {
            @Override
            public void onResponse(Call<notifikasiResponse> call, Response<notifikasiResponse> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    notifikasiResponse fcmResponse = response.body();
                    // Check the success and failure counts in fcmResponse
                } else {
                    // Handle failure response
                }
            }

            @Override
            public void onFailure(Call<notifikasiResponse> call, Throwable t) {
                // Handle request failure
            }
        });
    }
}
