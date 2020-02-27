package com.dongldh.memoapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_memo_order_dialog_fragment.view.*
import kotlinx.android.synthetic.main.item_memo.view.*
import java.text.SimpleDateFormat
import java.util.*

class MemoOrderDialogFragment : DialogFragment() {
    lateinit var recyclerView: RecyclerView
    var list: MutableList<DataItemMemo> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        val view = inflater.inflate(R.layout.activity_memo_order_dialog_fragment, container, false)
        recyclerView = view.recyclerView
        selectDB()

        view.button_cancel.setOnClickListener() {
            dismiss()
        }

        return view
    }

    private fun selectDB() {
        list = arrayListOf()
        val helper = DBHelper(context as Context)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select * from t_memo order by date desc", null)

        while (cursor.moveToNext()) {
            val num = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            val date = cursor.getLong(3)

            val memo_date = SimpleDateFormat("yyyy. MM. dd").format(Date(date))
            val memo_time = SimpleDateFormat("aa hh:mm").format(Date(date))

            val item = DataItemMemo(title, content, memo_date, memo_time, num)
            list.add(item)
        }
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = MemoAdapter(list)
    }

    class MemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_memo = view.text_memo
        val text_memo_title = view.text_memo_title
        val text_memo_date = view.text_memo_date
        val text_memo_time = view.text_memo_time

    }

    inner class MemoAdapter(val list: MutableList<DataItemMemo>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            return MemoViewHolder(layoutInflater.inflate(R.layout.item_memo, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val memoVO = list[position]
            val viewHolder = holder as MemoViewHolder

            viewHolder.text_memo_title.text = memoVO.title
            viewHolder.text_memo_date.text = memoVO.memo_date
            viewHolder.text_memo_time.text = memoVO.memo_time

        }
    }
}
