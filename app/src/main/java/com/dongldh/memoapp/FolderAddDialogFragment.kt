package com.dongldh.memoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_add_memo_dialog_fragment, container, false)
        isCancelable = false
        view.button_cancel_folder_add_memo.setOnClickListener() {
            dismiss()
        }

        view.button_ok_folder_add_memo.setOnClickListener() {
            dismiss()
            // editText에 있는 내용을 새롭게 db에 넣어줘야됨
        }
        return view
    }
}
