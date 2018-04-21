package com.simple.youtuberemote.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import static android.content.Context.WIFI_SERVICE;


public class Utils
{
  public static String getBroadcastAddress(Context context) {
    WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
    String result = "";
    if (wm != null) {
      String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
      String arr[] = TextUtils.split(ip, "\\.");
      arr[3] = "255";
      result = TextUtils.join(".", arr);
    }


    return result;
  }
}
