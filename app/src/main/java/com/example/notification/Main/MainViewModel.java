package com.example.notification.Main;

import android.content.Context;

import com.example.notification.Model.DataModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;

public class MainViewModel
{
    private Context context;
    private Repository repository;
    //repository is a class that defines which of the methods(local,Api)
    //has to be called .
    public MainViewModel(Context context)
    {
        this.context=context;
        repository=new Repository(context);
    }

    public Observable<Boolean> sendDataModel(int userId)
    {
        return repository.sendDataModel(userId);
    }
    public void saveDataInDB(String userCode,String second,String alramPath)
    {
        repository.saveData(userCode,second,alramPath);
    }
    public List<DataModel> getUserData()
    {
        return repository.getSoredData();
    }
    public void updateUserData(int id, String userCode, String second, String alarmPath)
    {
        repository.updateData(id,userCode,second,alarmPath);
    }




}
