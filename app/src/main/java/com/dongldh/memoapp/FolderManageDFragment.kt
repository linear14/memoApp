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
import kotlinx.android.synthetic.main.activity_folder_lock_caution_dialog.view.*
import kotlinx.android.synthetic.main.activity_folder_lock_dialog.*
import kotlinx.android.synthetic.main.activity_folder_lock_dialog.view.*
import kotlinx.android.synthetic.main.activity_folder_manage_d_fragment.view.*
import kotlinx.android.synthetic.main.activity_folder_manage_d_fragment.view.button_cancel
import kotlinx.android.synthetic.main.activity_folder_manage_d_fragment.view.folder_manage_lock_button
import kotlinx.android.synthetic.main.activity_folder_name_edit_d_frament.*
import kotlinx.android.synthetic.main.activity_folder_name_edit_d_frament.view.*

class FolderManageDFragment : DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_manage_d_fragment, container, false)
        val folder = arguments?.getString("title")

        val helper = DBHelper(this.context as Context)
        val db = helper.writableDatabase
        val cursor = db.rawQuery("select password from t_menu where title='${folder}'", null)
        cursor.moveToNext()

        val password: String? = cursor.getString(0)
        val havePassword = !password.isNullOrEmpty()
        db.close()
        isCancelable = false
        view.textView.text = folder

        if(havePassword) {
            view.folder_manage_lock_button.text = "폴더 잠금 해제"
        } else {
            view.folder_manage_lock_button.text = "폴더 잠금"
        }


        // 잠금 설정 버튼 or 해제 버튼
        view.folder_manage_lock_button.setOnClickListener() {
            when(havePassword) {
                // 잠금 해제
                true -> {
                    val bundle = Bundle()
                    bundle.putString("folder", folder)
                    bundle.putString("password", password)

                    val folderUnlockDialog = FolderUnlockDialog()
                    folderUnlockDialog.arguments = bundle
                    folderUnlockDialog.show(
                        activity?.supportFragmentManager as FragmentManager, "dialog_event"
                    )

                }
                // 잠금 설정
                false -> {
                    val bundle = Bundle()
                    bundle.putString("folder", folder)
                    when(App.preferences.isOpenCaution) {
                        true -> {
                            val folderLockCautionDialog = FolderLockCautionDialog()
                            folderLockCautionDialog.arguments = bundle
                            folderLockCautionDialog.show(
                                activity?.supportFragmentManager as FragmentManager, "dialog_event"
                            )
                        }
                        false -> {
                            val folderLockDialog = FolderLockDialog()
                            folderLockDialog.arguments = bundle
                            folderLockDialog.show(
                                activity?.supportFragmentManager as FragmentManager, "dialog_event"
                            )
                        }
                    }
                }
            }
            dismiss()
        }

        // 편집 버튼
        view.folder_manage_edit_button.setOnClickListener() {
            val bundle = Bundle()
            bundle.putString("folder", folder)
            val folderNameEditDFragment = FolderNameEditDFragment()
            folderNameEditDFragment.arguments = bundle
            folderNameEditDFragment.show(
                activity?.supportFragmentManager as FragmentManager, "dialog_event"
            )
            dismiss()
        }

        // 삭제 버튼
        view.folder_manage_del_button.setOnClickListener(){
            val db = helper.writableDatabase
            db.delete("t_menu", "title=?", arrayOf(folder))
            db.delete("t_memo", "folder=?", arrayOf(folder))
            db.close()
            dismiss()
            activity?.recreate()
        }

        view.button_cancel.setOnClickListener(){
            dismiss()
        }
        return view
    }
}

// 편집 버튼 눌렀을 때 보여지는 다이얼로그
class FolderNameEditDFragment: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_name_edit_d_frament, container, false)
        val helper = DBHelper(this.context as Context)
        isCancelable = false

        view.edit_folder_name.setText(arguments?.getString("folder"))

        view.button_cancel_edit.setOnClickListener() {
            dismiss()
        }

        view.button_ok_edit.setOnClickListener(){
            val db = helper.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("title", edit_folder_name.text.toString())

            // db를 바꾸는 게 좋을 것 같은데 넘 헷갈려서 그만.. ㅠㅠ
            val contentValuess = ContentValues()
            contentValuess.put("folder", edit_folder_name.text.toString())

            db.update("t_menu", contentValues, "title=?", arrayOf(arguments?.getString("folder")))
            db.update("t_memo", contentValuess, "folder=?", arrayOf(arguments?.getString("folder")))
            db.close()
            dismiss()
            activity?.recreate()
        }

        return view
    }
}


// 폴더 잠금 눌렀을 때 보여지는 다이얼로그 (isOpenCaution = true)
class FolderLockCautionDialog: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_lock_caution_dialog, container, false)
        isCancelable = false

        view.button_next.setOnClickListener() {
            App.preferences.isOpenCaution = !view.checkBox.isChecked
            val bundle = Bundle()
            bundle.putString("folder", arguments?.getString("folder"))
            val folderLockDialog = FolderLockDialog()
            folderLockDialog.arguments = bundle
            folderLockDialog.show(
                activity?.supportFragmentManager as FragmentManager, "dialog_event"
            )

            dismiss()
        }
        return view
    }
}

// 폴더 잠금 눌렀을 때 보여지는 다이얼로그 (isOpenCaution = false)
class FolderLockDialog: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_lock_dialog, container, false)
        isCancelable = false

        view.button_cancel_lock.setOnClickListener() {
            dismiss()
        }

        view.button_ok_lock.setOnClickListener() {
            if(password.text.toString().isNullOrEmpty()) {
                Toast.makeText(this.context, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle()
                bundle.putString("password", password.text.toString())
                bundle.putString("folder", arguments?.getString("folder"))

                val folderLockVerifyDialog = FolderLockVerifyDialog()
                folderLockVerifyDialog.arguments = bundle
                folderLockVerifyDialog.show(
                    activity?.supportFragmentManager as FragmentManager, "dialog_event"
                )
                dismiss()
            }
        }
        return view

    }
}

// 비밀번호 확인
class FolderLockVerifyDialog: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_lock_dialog, container, false)
        isCancelable = false

        view.textView_password.text = "비밀번호 확인"
        view.password.hint = "다시 한 번 입력하세요"
        view.button_cancel_lock.setOnClickListener() {
            dismiss()
        }

        view.button_ok_lock.setOnClickListener(){
            val password = password.text.toString()
            when {
                password.isNullOrEmpty() ->
                    Toast.makeText(this.context, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                !arguments?.getString("password").equals(password) ->
                    Toast.makeText(this.context, "비밀번호가 틀립니다", Toast.LENGTH_SHORT).show()
                else -> {
                    // 비밀번호 성공. 등록해야됨
                    val folder = arguments?.getString("folder")
                    val helper = DBHelper(this.context as Context)
                    val db = helper.writableDatabase

                    val content = ContentValues()
                    content.put("password", password)
                    db.update("t_menu", content, "title=?", arrayOf(folder))
                    db.close()
                    dismiss()
                    Toast.makeText(this.context, "비밀번호 등록 완료", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
}

// 폴더 잠금 해제 다이얼로그
class FolderUnlockDialog: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_folder_lock_dialog, container, false)
        isCancelable = false
        val password_answer = arguments?.getString("password")

        view.textView_password.text = "폴더 잠금 해제"
        view.password.hint = "비밀번호를 입력하세요"
        view.button_cancel_lock.setOnClickListener() {
            dismiss()
        }

        view.button_ok_lock.setOnClickListener() {
            when {
                password.text.toString().isNullOrEmpty() ->
                    Toast.makeText(this.context, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                !password.text.toString().equals(password_answer) ->
                    Toast.makeText(this.context, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                else -> {
                    val helper = DBHelper(this.context as Context)
                    val db = helper.writableDatabase
                    val content = ContentValues()
                    content.put("password", "")
                    db.update("t_menu", content, "title=?", arrayOf(arguments?.getString("folder")))
                    db.close()
                    Toast.makeText(this.context, "잠금 해제 되었습니다", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
        return view

    }
}
