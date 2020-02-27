package com.dongldh.memoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_memo_detail.*

class MemoDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)

        text_memo_detail_title.text = intent.getStringExtra("title")
        text_memo_detail_content.text = intent.getStringExtra("content")

    }
}
