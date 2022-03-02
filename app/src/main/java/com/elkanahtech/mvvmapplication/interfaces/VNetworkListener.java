package com.elkanahtech.mvvmapplication.interfaces;

public interface VNetworkListener {
   void onProcessComplete(String jsonString, int callCode);
   void onErrorOccur(String jsonString, int callCode);
}
