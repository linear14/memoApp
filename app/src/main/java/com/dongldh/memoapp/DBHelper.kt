package com.dongldh.memoapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, "memoDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val memoSQL= "create table t_memo " +
                "(num integer primary key autoincrement, title, content, date)"
                    // 게시글 등록할 때, date 함수로 시간 받아와서 date 컬럼에 집어 넣자
                    // https://stackoverflow.com/questions/754684/how-to-insert-a-sqlite-record-with-a-datetime-set-to-now-in-android-applicatio

        val accountSQL = "create table t_account " +
                "(num integer primary key autoincrement, title, bank, account, name, content)"

        val memoInitSQL = "insert into t_memo (title, content, date) values ('환영합니다!', '똑똑노트는 실생활에서 사용되는 각종 정보를 쉽고 편하게 정리할 수 있도록 만든 어플입니다." +
                "\n\n똑똑노트는 무료인가요? 네. 무료입니다. 대신 주위 분들에게 많이 많이 알려주세요! 개발자도 먹고 살아야 하니까요!!', '${System.currentTimeMillis()}')"

        db?.execSQL(memoSQL)
        db?.execSQL(accountSQL)
        db?.execSQL(memoInitSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table t_memo")
        db?.execSQL("drop table t_account")
        onCreate(db)
    }

}