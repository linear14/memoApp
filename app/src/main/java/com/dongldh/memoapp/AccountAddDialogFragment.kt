package com.dongldh.memoapp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_account_add_dialog_fragment.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation.*

class AccountAddDialogFragment : DialogFragment() {
    lateinit var _view: View

    // https://developer88.tistory.com/132 -> 다이어로그 프래그먼트 구현
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.activity_account_add_dialog_fragment, container, false)
        isCancelable = false

        val helper = DBHelper(this.context as Context)
        val db = helper.writableDatabase

        _view.button_ok.setOnClickListener() {
            if(_view.text_input_title.text.toString().isNullOrEmpty() || _view.text_input_bank.text.toString().isNullOrEmpty() ||
                _view.text_input_content.text.toString().isNullOrEmpty() || _view.text_input_name.text.toString().isNullOrEmpty()) {
                Toast.makeText(context, "기본 항목을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                val contentValues = ContentValues()
                contentValues.put("title", _view.text_input_title.text.toString())
                contentValues.put("bank", _view.text_input_bank.text.toString())
                contentValues.put("account", _view.text_input_account.text.toString())
                contentValues.put("name", _view.text_input_name.text.toString())
                contentValues.put("content", _view.text_input_content.text.toString())

                db.insert("t_account", null, contentValues)
                db.close()

                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment, AccountFragment())?.commit()

                dismiss()
            }
        }

        _view.button_cancel.setOnClickListener(){
            dismiss()
        }
        return _view
    }
}
