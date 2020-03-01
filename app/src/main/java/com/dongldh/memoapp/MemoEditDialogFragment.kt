package com.dongldh.memoapp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_memo_edit_dialog_fragment.view.*

class MemoEditDialogFragment : DialogFragment() {
    // 여기다가 folder를 키값으로 하는 bundle 받으면 왜 data가 전달이 안될까? -> onCreateView에다가 해야됨
    // val folder = arguments?.getString("folder")
    val memoFragment = MemoFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        val view = inflater.inflate(R.layout.activity_memo_edit_dialog_fragment, container, false)
        val memoVO = (arguments?.getSerializable("memoVO")) as DataItemMemo
        val helper = DBHelper(this.context as Context)

        val folder = arguments?.getString("folder")
        val bundle = Bundle()
        bundle.putString("folder", folder)

        memoFragment.arguments = bundle

        view.memo_menu_edit.setOnClickListener() {
            val intent = Intent(context, MemoEditActivity::class.java)
            intent.putExtra("num", memoVO.num)
            intent.putExtra("title", memoVO.title)
            intent.putExtra("content", memoVO.content)
            intent.putExtra("memo_date", memoVO.memo_date)
            intent.putExtra("memo_time", memoVO.memo_time)
            startActivityForResult(intent, 20)
        }


        view.memo_menu_del.setOnClickListener() {
            val db = helper.writableDatabase
            db.delete("t_memo","num=?", arrayOf(memoVO.num.toString()))
            db.close()
            dismiss()

            //log
            // Toast.makeText(this.context, "받아온 값 : $folder", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment, memoFragment)?.commit()
        }

        view.memo_menu_alarm.setOnClickListener() {
            val bundle = Bundle()
            bundle.putSerializable("memoVO", memoVO)

            val alarmDialogFragment = MemoAlarmDialogFragment()
            alarmDialogFragment.arguments = bundle

            dismiss()
            alarmDialogFragment.show (
                activity?.supportFragmentManager as FragmentManager, "dialog_event"
            )

        }

        view.memo_menu_share.setOnClickListener() {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "${memoVO.title}\n\n${memoVO.content}") // text는 공유하고 싶은 글자

            val chooser = Intent.createChooser(intent, "공유")
            startActivity(chooser)

            dismiss()
        }

        view.memo_menu_copy.setOnClickListener() {
            val db = helper.writableDatabase
            val date = System.currentTimeMillis()

            val contentValues = ContentValues()
            contentValues.put("title", memoVO.title)
            contentValues.put("content", memoVO.content)
            contentValues.put("date", date.toString())
            contentValues.put("folder", folder)

            db.insert("t_memo", null, contentValues)
            db.close()
            dismiss()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment, memoFragment)?.commit()
        }

        view.memo_menu_order.setOnClickListener() {
            val memoOrderDialogFragment = MemoOrderDialogFragment()
            dismiss()

            memoOrderDialogFragment.show(
                activity?.supportFragmentManager as FragmentManager, "dialog_event"
            )
        }

        view.button_cancel.setOnClickListener() {
            dismiss()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20 && resultCode == Activity.RESULT_OK) {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment, memoFragment)?.commit()
            dismiss()
        }
    }
}
