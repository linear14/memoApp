package com.dongldh.memoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_memo.*
import kotlinx.android.synthetic.main.navigation.*
    lateinit var inputMethodManager: InputMethodManager
    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // deleteDatabase("memoDB")  // 만들어져있던 데이터베이스 삭제 -> 새로운 기능 추가 시 활성화

            inputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            setContentView(R.layout.activity_main)
            title = "텍스트 메모"
            supportFragmentManager.beginTransaction().replace(R.id.fragment, MemoFragment()).commit()

            button_account.setOnClickListener() {
                title = "계좌번호 관리"
                supportFragmentManager.beginTransaction().replace(R.id.fragment, AccountFragment()).commit()
                drawer_layout.closeDrawer(drawer)
            }
            button_memo.setOnClickListener(){
                title = "텍스트 메모"
                supportFragmentManager.beginTransaction().replace(R.id.fragment, MemoFragment()).commit()
                drawer_layout.closeDrawer(drawer)
            }
        }


        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        inputMethodManager.hideSoftInputFromWindow(input_edit.windowToken, 0)
        when (item.itemId) {
            R.id.action_open_drawer -> {
                drawer_layout.openDrawer(drawer)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}




