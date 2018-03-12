package com.example.javier.izvactivities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IzvActivitiesService {
    @GET("izv_activities?_sort=year,month,day")
    Call<List<IzvActivity>> getActivities();

    @POST("izv_activities")
    Call<Void> addActivity(@Body IzvActivity activity);

    @PUT("izv_activities/{id}")
    Call<Void> editActivity(@Path("id") int id, @Body IzvActivity activity);

    @DELETE("izv_activities/{id}")
    Call<Void> deleteActivity(@Path("id") int id);

    @GET("teachers")
    Call<String[]> getTeachers();

    @GET("groups")
    Call<String[]> getGroups();
}
