package com.dongldh.memoapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.content.ContentValues
import kotlinx.android.synthetic.main.activity_memo_add.*
import java.text.SimpleDateFormat
import java.util.*

class MemoAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_add)

        title = "메모 쓰기"

        button_memo_add.setOnClickListener(){
            if(!text_input_memo_title.text.toString().isNullOrEmpty() && !text_input_memo_content.text.toString().isNullOrEmpty()) {
                val time = System.currentTimeMillis()

                val helper = DBHelper(this)
                val db = helper.writableDatabase

                val contentValues = ContentValues()
                contentValues.put("title", text_input_memo_title.text.toString())
                contentValues.put("content", text_input_memo_content.text.toString())
                contentValues.put("date", time.toString())

                // log
                // Toast.makeText(this, time.toString(), Toast.LENGTH_LONG).show()
                db.insert("t_memo", null, contentValues)
                db.close()

                setResult(Activity.RESULT_OK)
                finish()

            } else {
                Toast.makeText(this, "항목을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
