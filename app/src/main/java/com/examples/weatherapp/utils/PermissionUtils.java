package com.photon.weatherapp.utils;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

import android.content.Context;

import pub.devrel.easypermissions.EasyPermissions;

public class PermissionUtils {

    public static boolean hasLocationPermission(Context context){
        return EasyPermissions.hasPermissions(context, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION);
    }

}
