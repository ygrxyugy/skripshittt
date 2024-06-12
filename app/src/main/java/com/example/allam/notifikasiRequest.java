package com.example.allam;

public class notifikasiRequest {
    private String to;
    private Notification notification;

    public notifikasiRequest(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public static class Notification {
        private String title;
        private String body;

        public Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}