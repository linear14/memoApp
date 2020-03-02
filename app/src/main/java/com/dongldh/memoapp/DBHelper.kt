package com.dongldh.memoapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, "memoDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val menuSQL = "create table t_menu " +
                "(num integer primary key autoincrement, title, password)"
        val menuInitSQL = "insert into t_menu (title) values ('일반 메모')"



        // 게시글 등록할 때, date 함수로 시간 받아와서 date 컬럼에 집어 넣자
        // https://stackoverflow.com/questions/754684/how-to-insert-a-sqlite-record-with-a-datetime-set-to-now-in-android-applicatio
        val memoSQL = "create table t_memo " +
                "(num integer primary key autoincrement, title, content, date, folder)"

        val memoInitSQL1 = "insert into t_memo (title, content, date, folder) values ('환영합니다!', '똑똑노트는 실생활에서 사용되는 각종 정보를 쉽고 편하게 정리할 수 있도록 만든 어플입니다." +
                "\n\n똑똑노트는 무료인가요? 네. 무료입니다. 대신 주위 분들에게 많이 많이 알려주세요! 개발자도 먹고 살아야 하니까요!!', '${System.currentTimeMillis()}', '일반 메모')"
        val memoInitSQL2 = "insert into t_memo (title, content, date, folder) values ('똑똑노트 사용법', '알아서 사용하세용', '${System.currentTimeMillis()}', '일반 메모')"
        val memoInitSQL3 = "insert into t_memo (title, content, date, folder) values ('스마트폰 사용 시 유용한 팁', '계산기 어플을 다운받아 핸드폰에 내장된 기본 계산기 어플의 테마를 바꿔보세요!', '${System.currentTimeMillis()}', '일반 메모')"
        val memoInitSQL4 = "insert into t_memo (title, content, date, folder) values ('핸드폰 변경 시 자료 옮기는 법', '추후 업데이트 예정', '${System.currentTimeMillis()}', '일반 메모')"



        val accountSQL = "create table t_account " +
                "(num integer primary key autoincrement, title, bank, account, name, content)"


        db?.execSQL(menuSQL)
        db?.execSQL(menuInitSQL)
        db?.execSQL(memoSQL)
        db?.execSQL(accountSQL)
        db?.execSQL(memoInitSQL1)
        db?.execSQL(memoInitSQL2)
        db?.execSQL(memoInitSQL3)
        db?.execSQL(memoInitSQL4)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table t_memo")
        db?.execSQL("drop table t_account")
        onCreate(db)
    }

}