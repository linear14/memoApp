package com.dongldh.memoapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_account_detail_dialog_fragment.view.*

class AccountDetailDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_account_detail_dialog_fragment, container, false)
        view.text_account_detail_title.text = arguments?.getString("title")
        view.text_account_detail_content.text = arguments?.getString("content")

        // log
        // Toast.makeText(context, arguments?.getString("title"), Toast.LENGTH_SHORT).show()

        isCancelable = false

        view.button_ok.setOnClickListener() {
            dismiss()
        }

        return view
    }
}
