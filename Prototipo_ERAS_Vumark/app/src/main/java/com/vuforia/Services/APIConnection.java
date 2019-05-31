package com.vuforia.Services;

import com.vuforia.Enums.HttpConnectionMethodEnum;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIConnection
{
    /**
     * Method that sends a request to a URL
     * @param url the URL that you want to send a request
     * @param requestBody the body of the request
     * @param requestType the type of the request
     * @return <code>null</code> if the url was pass null; <code>Response</code> the response of the request
     * @throws IOException if something goes wrong
     */
    public static Response Request(String url, String requestBody, HttpConnectionMethodEnum requestType) throws IOException
    {
        if(!url.isEmpty())
        {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(url);

            builder.addHeader("Content-Type", "application/json");
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            RequestBody body = RequestBody.create(mediaType, requestBody);

            if(requestType == HttpConnectionMethodEnum.POST)
            {
                builder.post(body);
            }
            else
            {
                builder.get();
            }

            Request request = builder.build();

            return client.newCall(request).execute();
        }
        return null;
    }
}
