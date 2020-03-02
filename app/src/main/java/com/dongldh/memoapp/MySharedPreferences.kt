package com.dongldh.memoapp

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    val preferences: SharedPreferences = context.getSharedPreferences("preferences", 0)

    var isOpenCaution: Boolean
        get() = preferences.getBoolean("preferences", true)
        set(value) = preferences.edit().putBoolean("preferences", value).apply()

}