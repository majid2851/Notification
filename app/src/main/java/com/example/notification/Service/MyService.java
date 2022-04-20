

package com.example.notification.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.notification.Main.MainViewModel;
import com.example.notification.MainActivity;
import com.example.notification.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class MyService extends Service
{
    private MainViewModel mainViewModel;
    private CompositeDisposable disposable=new CompositeDisposable();
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    Context context;
    boolean check=false;


    public MyService(  )
    {

    }
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        mainViewModel=new MainViewModel(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sendDataToServer();

        return START_STICKY;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();

    }
    private void startForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }

    private String getTime()
    {
        //get time
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public void sendDataToServer()
    {
        int userId=Integer.parseInt(mainViewModel.getUserData().get(0).getUserCode());//user code
        int delay= Integer.parseInt(mainViewModel.getUserData().get(0).getSecond());
        String startTime=getTime();

        mainViewModel.sendDataModel(userId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).retry(19999999)
                .repeatWhen(completed -> completed.
                        delay(delay, TimeUnit.SECONDS))
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Boolean response) {
                        Log.i("java","response:"+response);//pring response
                        if(response==true)
                        {
                            //play voice when response is TRUE.
                            audioPlayer(mainViewModel.getUserData().get(0).getAlarmAddress());
                                sendMessageToActivity("فعال",startTime);



                        }else
                        {
                            //if response is false(user code is wrong)
                            sendMessageToActivity("نام کاربری اشتباه",startTime);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("mag2851",e.toString());
                        //connection is lost, or user clicks on stop btn;
                        sendMessageToActivity("غیرفعال",startTime);


                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }




    public void audioPlayer(String path){
        //play a voice from its path
        RingtoneManager.getRingtone(this, Uri.parse(path)).play();
    }


    public void sendMessageToActivity(String situation, String time) {
        Intent intent = new Intent("sendInform");
        // You can also include some extra data.
        intent.putExtra("time", time);
        intent.putExtra("situation", situation);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
