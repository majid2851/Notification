package com.example.notification.Main;

import android.content.Context;

import com.example.notification.Model.DataModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;

public class Repository implements DataSource
{
    private Context context;
    private LocalDataSource localDataSource;
    private ApiDataSource apiDataSource=new ApiDataSource();
    public Repository(Context context)
    {
        localDataSource=new LocalDataSource(context);
    }
    @Override
    public Observable<Boolean> sendDataModel(int userId)
    {
        return apiDataSource.sendDataModel(userId);
    }

    @Override
    public void saveData(String userCode, String second,String alarmPath) {
        localDataSource.saveData(userCode,second,alarmPath);
    }

    @Override
    public List<DataModel> getSoredData() {
        return localDataSource.getSoredData();
    }

    @Override
    public void updateData(int id, String userCode, String second, String alarmPath) {
        localDataSource.updateData(id,userCode,second,alarmPath);
    }
}
