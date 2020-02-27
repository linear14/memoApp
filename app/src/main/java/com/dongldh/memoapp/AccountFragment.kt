package com.dongldh.memoapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.android.synthetic.main.item_account.view.*

class AccountFragment : Fragment() {
    var list_account: MutableList<DataItemAccount> = mutableListOf()

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        recyclerView = view.recyclerView

        selectDB_account()

        view.button_fab.setOnClickListener() {
            AccountAddDialogFragment().show(
                activity?.supportFragmentManager as FragmentManager, "dialog_event")
        }
        return view
    }

/*    fun Fragment.addOnWindowFoucusChangeListener(callback: (hasFocus: Boolean) -> Unit) =
        view?.viewTreeObserver?.addOnWindowFocusChangeListener{callback.invoke(it)}*/


    fun selectDB_account() {
        list_account = arrayListOf()
        val helper = DBHelper(context as Context)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select * from t_account", null)

        while (cursor.moveToNext()) {
            val title = cursor.getString(1)
            val bank = cursor.getString(2)
            val account = cursor.getString(3)
            val name = cursor.getString(4)
            val content = cursor.getString(5)

            val item = DataItemAccount(title, bank, account, name, content)
            list_account.add(item)
        }

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = AccountAdapter(list_account)
    }


    class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_account = view.text_account
        val text_account_title = view.text_account_title
        val text_account_bank = view.text_account_bank
        val text_account_account = view.text_account_account
        val text_account_name = view.text_account_name
    }

    inner class AccountAdapter(val list: MutableList<DataItemAccount>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            return AccountViewHolder(
                layoutInflater.inflate(
                    R.layout.item_account,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        // Bundle 사용법
        // https://stackoverflow.com/questions/60369712/kotlin-android-data-transfer-from-fragment-to-dialogfragment/60369827?noredirect=1#comment106792441_60369827
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val accountVO = list[position]
            val viewHolder = holder as AccountViewHolder

            viewHolder.text_account_title.text = accountVO.title
            viewHolder.text_account_bank.text = accountVO.bank
            viewHolder.text_account_account.text = accountVO.account
            viewHolder.text_account_name.text = accountVO.name

            viewHolder.text_account.setOnClickListener() {
                // log - 작동
                // Toast.makeText(context, "${accountVO.title}, ${accountVO.content}", Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                bundle.putString("title", accountVO.title)
                bundle.putString("content", accountVO.content)

                val dialogFragment = AccountDetailDialogFragment()
                dialogFragment.arguments = bundle

                // 아래를 FragmentManager로 캐스팅 하지 않고 사용할 수 있는 방법?
                dialogFragment.show(activity?.supportFragmentManager as FragmentManager, "dialog_event")
            }

        }



    }

}
