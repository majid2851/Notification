package com.example.notification.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notification.Model.DataModel;

import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper
{
    SQLiteDatabase database;
    //parameters
    public static final String COL1="id";
    public static final String COL2="userCode";
    public static final String COL3="second";
    public static final String COL4="alarm";
    //table name
    public static final String TABLE1="data";


    //constructor
    public DataBaseHelper(Context context ) {
        super(context, TABLE1, null,  2);
    }

    //create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        ///creationg of the table
        db.execSQL("create table "+TABLE1+" (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userCode TEXT," + "second TEXT,"+"alarm TEXT)");
   }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE1);
        onCreate(db);
    }

    //insert data into sqlite database
    public void insertData(String userCode,String second,String alarmPath)
    {
        database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL2,userCode);
        values.put(COL3,second);
        values.put(COL4,alarmPath);
        database.insert(TABLE1,null,values);
    }


    //getting a list of user data
    public List<DataModel> getUserData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        List<DataModel> arrayList=new ArrayList<>();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE1  ,null);
        if(res!=null)
        {
            while (res.moveToNext())
            {
                DataModel model=new DataModel(res.getInt(0),res.getString(1)
                        ,res.getString(2),res.getString(3) );
                arrayList.add(model);
            }
            res.close();
            db.close();
        }
        return arrayList;

    }

    //modify user data and set new values
    public void updateScoreDB(int id,String userCode,String second,String alarmPath)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL2,userCode);
        values.put(COL3,second);
        values.put(COL4,alarmPath);


        db.update(TABLE1,values, COL1+ "='" + id+ "'"   ,null );

        if(db.isOpen()) db.close();

    }


}
