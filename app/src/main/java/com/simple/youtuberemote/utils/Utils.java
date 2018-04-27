package com.simple.youtuberemote.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.math.BigInteger;
import java.text.DecimalFormat;

import static android.content.Context.WIFI_SERVICE;


public class Utils
{
  public static String getBroadcastAddress(Context context)
  {
    WifiManager wm = (WifiManager) context.getApplicationContext()
                                          .getSystemService(WIFI_SERVICE);
    String result = "";
    if (wm != null) {
      String ip    = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
      String arr[] = TextUtils.split(ip, "\\.");
      arr[3] = "255";
      result = TextUtils.join(".", arr);
    }

    return result;
  }

  @SuppressLint ("DefaultLocale")
  public static String formatDuration(String duration)
  {
    String temp[] = duration.replace("PT", "")
                            .replace('H', ':')
                            .replace('M', ':')
                            .replace("S", "")
                            .split(":");
    if (temp.length == 3) {
      return String.format("%d:%02d:%02d", Integer.parseInt(temp[0]),
                                                            Integer.parseInt(temp[1]),
                                                                             Integer.parseInt(temp[2]));
    }
    else if (temp.length == 2) {
      return String.format("%d:%02d", Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
    }
    return String.format("00:%02d", Integer.parseInt(temp[0]));
  }
  public static String formatDecimal(BigInteger number) {
    DecimalFormat formatter = new DecimalFormat("#,###,###,###");
    return formatter.format(number);
  }
}
