package com.example.notification.Main;

import android.content.Context;

import com.example.notification.DataBase.DataBaseHelper;
import com.example.notification.Model.DataModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;

public class LocalDataSource implements DataSource
{
    //this class is used when we want to store or get data from
    //inner dataase

    private DataBaseHelper dataBaseHelper;
    private Context context;

    public LocalDataSource(Context context)
    {
        this.context=context;
        dataBaseHelper=new DataBaseHelper(context);
    }

    @Override
    public Observable<Boolean> sendDataModel(int userId) {
        return null;
        //this method is useless now ,
        // because the data has been gotten from server
    }

    @Override
    public void saveData(String userCode, String second,String alramPath) {
        dataBaseHelper.insertData(userCode,second,alramPath);
    }

    @Override
    public List<DataModel> getSoredData() {
        return dataBaseHelper.getUserData();
    }

    @Override
    public void updateData(int id, String userCode, String second, String alarmPath) {
        dataBaseHelper.updateScoreDB(id,userCode,second,alarmPath);
    }
}
