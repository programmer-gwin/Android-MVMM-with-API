package com.elkanahtech.mvvmapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.elkanahtech.mvvmapplication.databinding.ActivityMainBinding;
import com.elkanahtech.mvvmapplication.viewmodels.LoginViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(new LoginViewModel(this));
        activityMainBinding.executePendingBindings();
    }
}