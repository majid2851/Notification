package com.example.notification.Model;


import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService
{

    //with this request boolean value returns
    @GET("test.php")
      Observable<Boolean> sendDataModel(@Query("UserID") int userId);


}
