package com.example.notification.Model;

public class ApiProvider
{
    public static ApiService apiService;
    public static ApiService apiProvider()
    {
        if (apiService==null)
        {
            apiService=ApiClient.getClient().create(ApiService.class);
        }
        return apiService;

    }

}