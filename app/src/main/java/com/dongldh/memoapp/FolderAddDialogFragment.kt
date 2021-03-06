package com.dongldh.memoapp

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_folder_add_dialog_fragment.view.*
import kotlinx.android.synthetic.main.activity_folder_add_dialog_fragment.view.folder_add_text
import kotlinx.android.synthetic.main.activity_folder_add_memo_dialog_fragment.view.*

class FolderAddDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_add_dialog_fragment, container, false)
        isCancelable = false
        view.folder_add_text.setOnClickListener() {
            FolderAddMemoDialogFragment().show(activity?.supportFragmentManager as FragmentManager, "dialog_event")
            dismiss()
        }

        view.button_cancel.setOnClickListener() {
            dismiss()
        }

        return view
    }
}

class FolderAddMemoDialogFragment : DialogFragment() {
    var list: MutableList<String> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_add_memo_dialog_fragment, container, false)
        isCancelable = false
        view.button_cancel_folder_add_memo.setOnClickListener() {
            dismiss()
        }

        view.button_ok_folder_add_memo.setOnClickListener() {
            val helper = DBHelper(this.context as Context)
            val db = helper.writableDatabase
            val result = db.rawQuery("select title from t_menu", null)
            while(result.moveToNext()){
                list.add(result.getString(0))
            }

            val newFolder = view.input_folder_name.text.toString()

            when {
                newFolder.isNullOrEmpty() -> Toast.makeText(this.context, "폴더명을 입력하세요", Toast.LENGTH_SHORT).show()
                newFolder in list -> {
                    Toast.makeText(this.context, "폴더명 중복입니다.", Toast.LENGTH_SHORT).show()
                    view.input_folder_name.setText("")
                }
                else -> {
                    val contentValue = ContentValues()
                    contentValue.put("title", newFolder)
                    db.insert("t_menu", null, contentValue)
                    activity?.recreate()
                    dismiss()
                }
            }
        }
        return view
    }
}
