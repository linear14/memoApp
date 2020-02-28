package com.dongldh.memoapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_memo.*
import kotlinx.android.synthetic.main.item_edit.view.*
import kotlinx.android.synthetic.main.item_menu.view.text_menu
import kotlinx.android.synthetic.main.item_memo.view.*
import kotlinx.android.synthetic.main.item_menu.view.*
import kotlinx.android.synthetic.main.navigation.*
import java.text.SimpleDateFormat
import java.util.*

lateinit var inputMethodManager: InputMethodManager

class MainActivity : AppCompatActivity() {

    var list: MutableList<DataItemMenu> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // deleteDatabase("memoDB")  // 만들어져있던 데이터베이스 삭제 -> 새로운 기능 추가 시 활성화

        inputMethodManager =
            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setContentView(R.layout.activity_main)
        title = "텍스트 메모"
        supportFragmentManager.beginTransaction().replace(R.id.fragment, MemoFragment()).commit()

        selectDB()


        button_edit_folder.setOnClickListener() {
            val intent = Intent(this, FolderEditActivity::class.java)
            /*intent.putExtra("size", list.size)
            for(i in 0 until list.size) {
                intent.putExtra("list${i}", list[i])
            }*/
            startActivityForResult(intent, 30)
        }

        button_account.setOnClickListener() {
            title = "계좌번호 관리"
            supportFragmentManager.beginTransaction().replace(R.id.fragment, AccountFragment())
                .commit()
            drawer_layout.closeDrawer(drawer)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 30 && resultCode == Activity.RESULT_OK) {
            selectDB()
        }
    }

    private fun selectDB() {
        list = arrayListOf()
        val helper = DBHelper(this)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select * from t_menu", null)

        while (cursor.moveToNext()) {
            val title = cursor.getString(1)

            val item = DataItemMenu(title)
            list.add(item)
        }
        recyclerView_memo.layoutManager = LinearLayoutManager(this)
        recyclerView_memo.adapter = MenuAdapter(list)
    }

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_menu = view.text_menu
    }

    inner class MenuAdapter(val list: MutableList<DataItemMenu>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return MenuViewHolder(
                layoutInflater.inflate(R.layout.item_menu, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val menuVO = list[position]
            val viewHolder = holder as MenuViewHolder

            viewHolder.text_menu.text = menuVO.title

            viewHolder.text_menu.setOnClickListener() {
                title = viewHolder.text_menu.text
                supportFragmentManager.beginTransaction().replace(R.id.fragment, MemoFragment())
                    .commit()
                drawer_layout.closeDrawer(drawer)
            }
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




