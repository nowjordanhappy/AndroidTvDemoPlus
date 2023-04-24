package com.nowjordanhappy.core_ui

import android.content.Context
import android.content.pm.PackageManager

object PermissionsHandler{
    fun hasPermission(context: Context, permission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == context.packageManager.checkPermission(
            permission, context.packageName
        )
    }
}
