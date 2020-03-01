package com.dongldh.memoapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_folder_edit.*
import kotlinx.android.synthetic.main.item_edit.view.*

class FolderEditActivity : AppCompatActivity() {
    // 리스트의 값을 for문으로 받아서 쭉 보여준다.

    var list: MutableList<DataItemMenu> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_edit)
        title = "폴더 편집"

        selectDB()

        button_fab.setOnClickListener() {
            FolderAddDialogFragment().show(supportFragmentManager, "dialog_event")
        }



    }

    private fun selectDB() {
        list = mutableListOf()
        val helper = DBHelper(this)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select * from t_menu", null)

        while(cursor.moveToNext()) {
            val title = cursor.getString(1)
            val item = DataItemMenu(title)
            list.add(item)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FolderEditAdapter(list)
        setResult(Activity.RESULT_OK)
    }

    class FolderEditViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val text_menu_edit = view.text_menu_edit
    }

    inner class FolderEditAdapter(val list: MutableList<DataItemMenu>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return FolderEditViewHolder(layoutInflater.inflate(R.layout.item_edit, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val menuVO = list[position]
            val viewHolder = holder as FolderEditViewHolder

            viewHolder.text_menu_edit.text = menuVO.title
        }

    }


}
