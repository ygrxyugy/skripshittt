package com.example.allam;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.IdTokenProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class authUtil {
    public static String getAccessToken(String serviceAccountKeyPath) throws IOException {
        FileInputStream serviceAccount = new FileInputStream(serviceAccountKeyPath);

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount)
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
        credentials.refreshIfExpired();
        AccessToken token = credentials.getAccessToken();

        return token.getTokenValue();
    }

    public static void main(String[] args) {
        try {
            String accessToken = getAccessToken("app/serviceAccount.json");
            System.out.println("Access Token: " + accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
