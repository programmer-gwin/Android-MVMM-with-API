package com.elkanahtech.mvvmapplication.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.elkanahtech.mvvmapplication.models.JsonResponse;
import com.google.gson.Gson;

public class VUtils {

    private ProgressDialog progressDialog;
    Activity activity;

    public VUtils(Activity context){
        activity = context;
    }

    public void showDialog(String Title, String Message){
        activity.runOnUiThread(() -> {
            if (!activity.isFinishing()){
                new AlertDialog.Builder(activity)
                        .setTitle(Title).setMessage(Message).setCancelable(false)
                        .setPositiveButton("OK", null).show();
            }
        });
    }

    public AlertDialog showConfirmDialog(){
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Alert Dialog");
        builder.setMessage("Alert Dialog inside DialogFragment");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        return dialog;
    }

    public void showProgressDialog(String message, boolean status){
        if(status){
            if(TextUtils.isEmpty(message))
                progressDialog = ProgressDialog.show(activity, "", "Loading. Please wait...", true);
            else
                progressDialog = ProgressDialog.show(activity, "", message, true);
        }
        else
        if(progressDialog!=null)
            progressDialog.cancel();
    }

 /*   public static ArrayList<BookingsModel> DeserializeBookingList(String json) {
        try {
            Type userListType = new TypeToken<ArrayList<BookingsModel>>(){}.getType();
            return new Gson().fromJson(json, userListType);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }*/


    public void DisplayWithJsonResponse(String networkBody, Activity mactivity) {
        try {
            activity = mactivity;
            JsonResponse jsonResponse = new Gson().fromJson(networkBody, JsonResponse.class);
            if (jsonResponse != null) {
                if (jsonResponse.code.equals("00"))
                    showDialog("Success ", jsonResponse.msg);
                else
                    showDialog("Error " + jsonResponse.code, jsonResponse.msg);
            } else
                Toast.makeText(activity, "Deserialized response is null", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity, "Unable to deserialize response.", Toast.LENGTH_LONG).show();
        }
    }

    public void ToastText(String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

}
