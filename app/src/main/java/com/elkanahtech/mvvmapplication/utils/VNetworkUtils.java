package com.elkanahtech.mvvmapplication.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.elkanahtech.mvvmapplication.R;
import com.elkanahtech.mvvmapplication.data.VConstant;
import com.elkanahtech.mvvmapplication.interfaces.VNetworkListener;
import com.elkanahtech.mvvmapplication.models.JsonResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class VNetworkUtils {
    public static void postRequest(final Activity activity, final String actionName, final String contentBody, final VNetworkListener listener, final int callCode ){
        new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                try {
                    String APIKey = VConstant.apiKey;
                    String AppID = VConstant.appId;
                    final boolean isSecure = VConstant.BASE_URL.startsWith("https");
                    //prepare and send request
                    OutputStream os; BufferedReader br; int status;
                    HttpsURLConnection https = null;
                    HttpURLConnection http = null;
                    URL url = new URL(VConstant.BASE_URL + "POSTransactionv3ICC"); //or POSTransactionv3ICCFull or POSTransactionv3ICC
                    final String requestMethod = "POST";
                    if(isSecure){
                        https = (HttpsURLConnection) url.openConnection();
                        https.setDoOutput(true);
                        https.setDoInput(true);
                        https.setUseCaches(false);
                        https.setRequestMethod(requestMethod);
                        https.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        https.setConnectTimeout((int) TimeUnit.MINUTES.toMillis(2));
                        os = https.getOutputStream();
                    }
                    else{
                        http = (HttpURLConnection) url.openConnection();
                        http.setDoOutput(true);
                        http.setDoInput(true);
                        http.setUseCaches(false);
                        http.setRequestMethod(requestMethod);
                        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        http.setConnectTimeout((int) TimeUnit.MINUTES.toMillis(2));
                        os = http.getOutputStream();
                    }
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    final String postData = JWTEncryptionUtils.encrypt(contentBody, AppID, APIKey);
                    writer.write(postData);
                    writer.flush();
                    writer.close();
                    os.close();

                    status = isSecure ? https.getResponseCode() : http.getResponseCode();

                    if(status == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = isSecure ? https.getInputStream() : http.getInputStream();
                        br = new BufferedReader(new InputStreamReader((inputStream)));
                        //read response
                        StringBuilder sb = new StringBuilder();
                        String output;
                        while ((output = br.readLine()) != null) {
                            sb.append(output);
                        }
                        final String response = sb.toString();
                        final String responseDecrypt = JWTEncryptionUtils.decrypt(response, APIKey);
                        if(responseDecrypt != null)
                            activity.runOnUiThread(() -> listener.onProcessComplete(responseDecrypt, callCode));
                        else{
                            JsonResponse transactionResponse = new JsonResponse();
                            transactionResponse.code = "XT001";
                            transactionResponse.msg = "Communication Violation";
                            activity.runOnUiThread(() -> listener.onProcessComplete(new Gson().toJson(transactionResponse), callCode));
                        }
                    }
                    else{
                        InputStream inputStream = isSecure ? https.getErrorStream() : http.getErrorStream();
                        br = new BufferedReader(new InputStreamReader((inputStream)));
                        //read response
                        StringBuilder sb = new StringBuilder();
                        String output;
                        while ((output = br.readLine()) != null) {
                            sb.append(output);
                        }
                        final String response = sb.toString();
                        JsonResponse transactionResponse = new JsonResponse();
                        transactionResponse.code = String.valueOf(status);
                        transactionResponse.msg = "Error " + response;
                        activity.runOnUiThread(() -> listener.onProcessComplete(new Gson().toJson(transactionResponse), callCode));
                    }
                }catch (Exception e){
                    JsonResponse transactionResponse = new JsonResponse();
                    transactionResponse.code = "Internet Error";
                    transactionResponse.msg = "Could not connect to the internet.\nPlease check your connection";
                    activity.runOnUiThread(() -> listener.onErrorOccur(new Gson().toJson(transactionResponse), callCode));
                }
            }
        }).start();
    }

    /*public static void postRequestPOS(final Context context, final String address, final EmvCardData emvCardData, final MyCallBackListener listener, final int callCode ){
        new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                try {
                    String APIKey = XV.apiKey;
                    String AppID = XV.appId;
                    final boolean isSecure = address.startsWith("https");
                    //prepare and send request
                    OutputStream os; BufferedReader br; int status;
                    HttpsURLConnection https = null;
                    HttpURLConnection http = null;
                    URL url = new URL(address + "POSTransactionv3ICC"); //or POSTransactionv3ICCFull or POSTransactionv3ICC
                    final String requestMethod = "POST";
                    if(isSecure){
                        https = (HttpsURLConnection) url.openConnection();
                        https.setDoOutput(true);
                        https.setDoInput(true);
                        https.setUseCaches(false);
                        https.setRequestMethod(requestMethod);
                        https.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        https.setConnectTimeout((int) TimeUnit.MINUTES.toMillis(2));
                        os = https.getOutputStream();
                    }
                    else{
                        http = (HttpURLConnection) url.openConnection();
                        http.setDoOutput(true);
                        http.setDoInput(true);
                        http.setUseCaches(false);
                        http.setRequestMethod(requestMethod);
                        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        http.setConnectTimeout((int) TimeUnit.MINUTES.toMillis(2));
                        os = http.getOutputStream();
                    }
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    final String contentBody = new Gson().toJson(emvCardData);
                    final String postData = JWTEncryptionUtils.encrypt(contentBody, AppID, APIKey);
                    writer.write(postData);
                    writer.flush();
                    writer.close();
                    os.close();

                    status = isSecure ? https.getResponseCode() : http.getResponseCode();

                    if(status == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = isSecure ? https.getInputStream() : http.getInputStream();
                        br = new BufferedReader(new InputStreamReader((inputStream)));
                        //read response
                        StringBuilder sb = new StringBuilder();
                        String output;
                        while ((output = br.readLine()) != null) {
                            sb.append(output);
                        }
                        final String response = sb.toString();
                        final String responseDecrypt = JWTEncryptionUtils.decrypt(response, APIKey);
                        if(responseDecrypt != null)
                            listener.onProcessComplete(responseDecrypt, callCode);
                        else{
                            TransactionResponse transactionResponse = new TransactionResponse();
                            transactionResponse.responseCode = "XT001";
                            transactionResponse.responseDescription = "Communication Violation";
                            listener.onProcessComplete(new Gson().toJson(transactionResponse), callCode);
                        }
                    }
                    else{
                        InputStream inputStream = isSecure ? https.getErrorStream() : http.getErrorStream();
                        br = new BufferedReader(new InputStreamReader((inputStream)));
                        //read response
                        StringBuilder sb = new StringBuilder();
                        String output;
                        while ((output = br.readLine()) != null) {
                            sb.append(output);
                        }
                        final String response = sb.toString();
                        TransactionResponse transactionResponse = new TransactionResponse();
                        transactionResponse.responseCode = String.valueOf(status);
                        transactionResponse.responseDescription = "Error " + response;
                        listener.onErrorOccur(new Gson().toJson(transactionResponse), callCode);
                    }
                }catch (Exception e){
                    TransactionResponse transactionResponse = new TransactionResponse();
                    transactionResponse.responseCode = "Internet Error";
                    transactionResponse.responseDescription = "Could not connect to the internet.\nPlease check your connection";
                    listener.onErrorOccur(new Gson().toJson(transactionResponse), callCode);
                }
            }
        }).start();
    }*/
}