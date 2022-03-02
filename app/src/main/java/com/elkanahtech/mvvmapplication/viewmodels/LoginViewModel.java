package com.elkanahtech.mvvmapplication.viewmodels;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.elkanahtech.mvvmapplication.BR;
import com.elkanahtech.mvvmapplication.data.VConstant;
import com.elkanahtech.mvvmapplication.interfaces.VNetworkListener;
import com.elkanahtech.mvvmapplication.models.LoginModel;
import com.elkanahtech.mvvmapplication.utils.VNetworkUtils;
import com.elkanahtech.mvvmapplication.utils.VUtils;
import com.google.gson.Gson;

public class LoginViewModel extends BaseObservable implements VNetworkListener {

   @Bindable
   public String getUserEmail() {
      return loginModel.getEmail();
   }
   public void setUserEmail(String email) {
      loginModel.setEmail(email);
      notifyPropertyChanged(BR.userEmail);
   }

   @Bindable
   public String getUserPassword() {
      return loginModel.getPassword();
   }
   public void setUserPassword(String password) {
      loginModel.setPassword(password);
      notifyPropertyChanged(BR.userPassword);
   }

   private final LoginModel loginModel;
   private final Activity activity;
   private VUtils vUtils;
   public LoginViewModel(Activity mActivity) {
      activity=mActivity; loginModel = new LoginModel("","");
      vUtils=new VUtils(activity);
   }

   public void onButtonClicked() {
      if (isValid())
         VNetworkUtils.postRequest(activity, VConstant.LOGIN_ACTION_NAME, new Gson().toJson(loginModel), this, VConstant.LOGIN_CALL_CODE);
      else
         Toast.makeText(activity, "Pls enter Username and Password", Toast.LENGTH_SHORT).show();
   }

   public boolean isValid() {
      return !TextUtils.isEmpty(getUserEmail()) && !TextUtils.isEmpty(getUserPassword());
   }

   @Override
   public void onProcessComplete(String jsonString, int callCode) {
      if(callCode == VConstant.LOGIN_CALL_CODE)
         Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show();
      vUtils.DisplayWithJsonResponse(jsonString, activity);
   }

   @Override
   public void onErrorOccur(String jsonString, int callCode) {
      if(callCode == VConstant.LOGIN_CALL_CODE)
         Toast.makeText(activity, "Login failed", Toast.LENGTH_SHORT).show();
      vUtils.DisplayWithJsonResponse(jsonString, activity);
   }
}
