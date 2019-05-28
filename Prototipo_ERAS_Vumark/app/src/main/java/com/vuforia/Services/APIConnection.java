package com.vuforia.Services;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpHeaders;

public class APIConnection
{
    public static String Request(String url, String requestBody, int requestType) throws IOException
    {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        builder.addHeader("Content-Type", "application/json");
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(mediaType, requestBody);

        // TODO: mudar para enum
        if(requestType == 1)
        {
            builder.post(body);
        }
        else
        {
            builder.get();
        }

        Request request = builder.build();

        try
        {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
