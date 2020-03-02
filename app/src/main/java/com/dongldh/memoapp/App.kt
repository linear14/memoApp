package com.dongldh.memoapp

import android.app.Application
import android.content.Context

// Application 상속
class App : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun context() : Context {
            return instance!!.applicationContext
        }

        // companion object로 어디에서나 딱 한개만 만들 수 있는(싱글톤?) prefereces 생성
        lateinit var preferences : MySharedPreferences
    }

    // MainActivity보다 먼저 실행됨 -> SharedPreference를 어디서나 쓸 수 있음.
    override fun onCreate() {
        preferences = MySharedPreferences(applicationContext)
        super.onCreate()
    }

}