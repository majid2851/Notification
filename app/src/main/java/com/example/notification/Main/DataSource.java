package com.example.notification.Main;

import com.example.notification.Model.DataModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;

public interface DataSource
{



    Observable<Boolean> sendDataModel(int userId);

    void saveData(String userCode,String second,String alarmPath);

    List<DataModel> getSoredData();

    void updateData(int id,String userCode,String second,String alarmPath);

}
