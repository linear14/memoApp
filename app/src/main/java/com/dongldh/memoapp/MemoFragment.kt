package com.dongldh.memoapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_memo.view.*
import kotlinx.android.synthetic.main.item_memo.view.*
import java.text.SimpleDateFormat
import java.util.*

class MemoFragment : Fragment() {
    lateinit var _recyclerView: RecyclerView
    var list: MutableList<DataItemMemo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lifeCycle", "onCreate()")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("lifeCycle", "onCreateView()")
        var view = inflater.inflate(R.layout.fragment_memo, container, false)
        _recyclerView = view.recyclerView

        val floatingActionButton: FloatingActionButton = view.button_fab
        selectDB()

        view.input_edit.setOnFocusChangeListener{v, hasFocus ->
           when(hasFocus) {
               true -> inputMethodManager.showSoftInput(v, 0)
               false -> inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
           }
        }

        floatingActionButton.setOnClickListener() {
            val intent = Intent(context, MemoAddActivity::class.java)
            startActivityForResult(intent, 10)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            selectDB()
        }
    }

    private fun selectDB() {
        list = arrayListOf()
        val helper = DBHelper(context as Context)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select * from t_memo order by date desc", null)

        while (cursor.moveToNext()) {

            // db의 t_memo 테이블에서 title, content, date 받아옴
            // date는 또 다시 날짜와 시간으로 나누어서 list에 집어 넣어둠
            // 나중에 바인더에서 처리할 때 시간과 날짜를 텍스트로 나누어서 set 할거임

            // 일단 다음에는, 게시글 추가 할 때 date를 여기로 넘겨주는 것을 꼭 구현해야됨
            // 왜냐면 지금 date는 null 값이기 때문임 (물론 다른 값들도 null인 상태)
            val num = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            val date = cursor.getLong(3)

            val memo_date = SimpleDateFormat("yyyy. MM. dd").format(Date(date))
            val memo_time = SimpleDateFormat("aa hh:mm").format(Date(date))

            val item = DataItemMemo(title, content, memo_date, memo_time, num)
            list.add(item)
        }
        _recyclerView.layoutManager = LinearLayoutManager(this.context)
        _recyclerView.adapter = MemoAdapter(list)

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

            viewHolder.text_memo.setOnClickListener() {
                val intent = Intent(context, MemoDetail::class.java)
                intent.putExtra("title", memoVO.title)
                intent.putExtra("content", memoVO.content)
                startActivity(intent)
            }

            // memoVO등의 데이터 객체를 bundle에 집어넣는 방법
            // https://altongmon.tistory.com/814
            viewHolder.text_memo.setOnLongClickListener() {
                val bundle = Bundle()
                bundle.putSerializable("memoVO", memoVO)

                val memoEditDialogFragment = MemoEditDialogFragment()
                memoEditDialogFragment.arguments = bundle

                memoEditDialogFragment.show(
                    activity?.supportFragmentManager as FragmentManager, "dialog_event"
                )
                true
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("lifeCycle", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "onResume()")

        // 여기서 dialog가 dismiss()가 됐다는 것을 인식하게 만들어야됨

    }

    override fun onPause() {
        super.onPause()
        Log.d("lifeCycle", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("lifeCycle", "onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("lifeCycle", "onDestroyView()")
    }
}



