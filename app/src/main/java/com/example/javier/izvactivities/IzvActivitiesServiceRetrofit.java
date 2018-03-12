package com.example.javier.izvactivities;

public class IzvActivitiesServiceRetrofit {
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    //private static final String BASE_URL = "https://json-server-javierrodrigueziturriaga.c9users.io/";

    public static IzvActivitiesService getService() {
        return RetrofitClient.getClient(BASE_URL).create(IzvActivitiesService.class);
    }
}
