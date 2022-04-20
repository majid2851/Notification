package com.example.notification.Main;

import com.example.notification.Model.ApiProvider;
import com.example.notification.Model.DataModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;

public class ApiDataSource implements DataSource
{

    //this class is used ,when we want to get or send data to server
    //
    @Override
    public Observable<Boolean> sendDataModel(int userId) {
        return ApiProvider.apiProvider().sendDataModel(userId);
        //getting data from apiService
    }

    @Override
    public void saveData(String userCode, String second,String alarmPath) {
    //this method here is useless ,
        // because the data has been stored in inner database
    }

    @Override
    public List<DataModel> getSoredData() {
        return null;
        //this method here is useless ,
        // because the data has been stored in inner database
    }

    @Override
    public void updateData(int id, String userCode, String second, String alarmPath) {
        //this method here is useless ,
        // because the data has been stored in inner database
    }
}
