package com.example.notification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notification.Main.MainViewModel;
import com.example.notification.Model.DataModel;
import com.example.notification.Service.MyService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity
{

    MainViewModel mainViewModel;
    TextView alarmAddress;
    Button btnLogin,btnStop;
    private String chosenRingtone;
    private  TextView txtConnectionSituation;
    private  TextView txtUserName;
    private  TextView txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();

        if(mainViewModel.getUserData().size()>0){
            Intent intent=new Intent(MainActivity.this,MyService.class);
                stopService(intent);
                startService(intent);
                txtUserName.setText(mainViewModel.getUserData().get(0).getUserCode());

        }

        onImgRegisterClick();
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///stop service completely
                stopService(new Intent(MainActivity.this,MyService.class));
                txtConnectionSituation.setTextColor(Color.RED);
                txtConnectionSituation.setText("غیر فعال");
                txtTime.setText("");
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("sendInform"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String situation = intent.getStringExtra("situation");
            String time = intent.getStringExtra("time");

            txtTime.setText(time);
            if (situation.equals("فعال"))
            {
                //change color when connection is ready
                txtConnectionSituation.setTextColor(Color.GREEN);
            }else
            {
                //connection is lost
                txtConnectionSituation.setTextColor(Color.RED);
            }
            //show is connected or not
            txtConnectionSituation.setText(situation);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    private void onImgRegisterClick()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });
    }





    public void setupViews()
    {
        mainViewModel=new MainViewModel(this);
        btnLogin=findViewById(R.id.loginBtn);
        btnStop=findViewById(R.id.stopBtn);
        txtConnectionSituation=findViewById(R.id.connectionSituation);
        txtTime=findViewById(R.id.connectionTime);
        txtUserName=findViewById(R.id.userName);

   }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null) {
                this.chosenRingtone = uri.toString();//storing ringtone path
                alarmAddress.setText(this.chosenRingtone);
            } else {
                this.chosenRingtone = null;
            }
        }
    }

    public void showRegisterDialog()
    {
        //this dialog is for a time we want to register(in mainActivity)
        //and we can choose userCode,second and ringtone
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View v=getLayoutInflater().inflate(R.layout.login_layout,null);
        TextView tvAlarm;
        EditText  ediSecond,ediUser;
        Button submit,cancel;
        cancel=v.findViewById(R.id.cancel);
        tvAlarm=v.findViewById(R.id.selectAlarm);
        alarmAddress=v.findViewById(R.id.alarmAddress);
        ediSecond=v.findViewById(R.id.secondEditText);
        ediUser=v.findViewById(R.id.userEditText);
        submit=v.findViewById(R.id.submit);
        builder.setView(v);
        final AlertDialog dialog=builder.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        if(mainViewModel.getUserData().size()>0)//if user has registerd before
        {
            DataModel dataModel=mainViewModel.getUserData().get(0);
            ediSecond.setText(dataModel.getSecond());
            ediUser.setText(dataModel.getUserCode());
            alarmAddress.setText(dataModel.getAlarmAddress());
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        //
        //selection of alram
        tvAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                 startActivityForResult(intent, 5);
            }
        });
        //
        //store data after we click on submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      String second=ediSecond.getText().toString();
                String userCode=ediUser.getText().toString();
                String alarmPath=alarmAddress.getText().toString();

                if((second.length()>0)&& (userCode.length()>0)&& (alarmPath.length()>0))//when all fields are filled.
                {
                    if (mainViewModel.getUserData().size()>0)//if user has registered before
                    {
                        mainViewModel.updateUserData(1,userCode,second,alarmPath);
                        Intent intent=new Intent(MainActivity.this,MyService.class);
                        stopService(intent);
                        startService(intent);
                        dialog.dismiss();
                        txtUserName.setText(mainViewModel.getUserData().get(0).getUserCode());

                    }else//if its the first time user wants to add his informations.
                    {
                        mainViewModel.saveDataInDB(userCode,second,alarmPath);
                        dialog.dismiss();//close dialog
                        startService(new Intent(MainActivity.this,MyService.class));
                        txtUserName.setText(mainViewModel.getUserData().get(0).getUserCode());

                    }


                }else// when all fileds are not filled!!
                {
                    Toast.makeText(MainActivity.this,
                            "please fill all!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.setCancelable(false);//we can't cancel dialog by touching main screen


    }




}