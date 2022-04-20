package com.example.notification.Model;

public class DataModel
{
    int id;
    String second;
    String userCode;
    String alarmAddress;

    public DataModel(int id,String userCode,String second, String alarmAddress) {
        this.id=id;
        this.second = second;
        this.userCode = userCode;
        this.alarmAddress = alarmAddress;
    }

    public void setId(int id)
    {
        this.id =id;
    }
    public int getId()
    {
        return id;
    }

    public String getAlarmAddress() {
        return alarmAddress;
    }

    public void setAlarmAddress(String alarmAddress) {
        this.alarmAddress = alarmAddress;
    }

    public void setSecond(String second)
    {
        this.second=second;
    }

    public String getSecond()
    {
        return second;
    }
    public void setUserCode(String userCode)
    {
        this.userCode=userCode;
    }
    public String getUserCode()
    {
        return userCode;
    }
}
