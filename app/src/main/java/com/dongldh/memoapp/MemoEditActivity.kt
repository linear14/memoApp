package com.dongldh.memoapp

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_memo_add.*

class MemoEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_add)

        title = "메모 수정"

        text_input_memo_title.setText(intent.getStringExtra("title"), TextView.BufferType.EDITABLE)
        text_input_memo_content.setText(intent.getStringExtra("content"), TextView.BufferType.EDITABLE)

        button_memo_add.setOnClickListener() {
            val helper = DBHelper(this)
            val db = helper.writableDatabase

            val contentValues = ContentValues()
            contentValues.put("title", text_input_memo_title.text.toString())
            contentValues.put("content", text_input_memo_content.text.toString())

            db.update("t_memo", contentValues, "num=?", arrayOf(intent.getIntExtra("num", 0).toString()))
            db.close()

            setResult(Activity.RESULT_OK)
            finish()

        }
    }
}