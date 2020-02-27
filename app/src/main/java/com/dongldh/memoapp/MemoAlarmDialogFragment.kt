package com.dongldh.memoapp

import android.app.*
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_memo_alarm_dialog_fragment.view.*
import kotlinx.android.synthetic.main.activity_memo_alarm_dialog_fragment.view.button_cancel
import kotlinx.android.synthetic.main.activity_memo_alarm_timesetting_dialog_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*

// 여기서 다 처리하자 그냥

class MemoAlarmDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_memo_alarm_dialog_fragment, container, false)
        val memoVO = (arguments?.getSerializable("memoVO")) as DataItemMemo

        // 알림 시간 설정
        view.memo_alarm_time_setting.setOnClickListener() {
            dismiss()
            val timeSettingDialogFragment = MemoAlarmTimeSettingDialogFragment()
            timeSettingDialogFragment.show(
                activity?.supportFragmentManager as FragmentManager, "dialog_event"
            )
        }

        // 알림 표시줄에 설정 -> 바로 실행
        view.memo_alarm_status_line.setOnClickListener() {
            dismiss()
            createNotification(memoVO.title, memoVO.content)
        }

        view.button_cancel.setOnClickListener() {
            dismiss()
        }
        return view
    }



    // https://sh-itstory.tistory.com/63 참고해서 만든 notification 알림
    fun createNotification(title: String, text: String) {
        val builder = NotificationCompat.Builder(this.context as Context, "default")

        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle(title)
        builder.setContentText(text)

        builder.color = Color.BLUE
        builder.setAutoCancel(true)

        // 그냥 해서 안 되어서.. context?를 붙였음
        // https://stackoverflow.com/questions/6843736/cannot-find-symbol-notification-service/9514002
        val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT))
        }
        notificationManager.notify(1, builder.build())
    }
}



// 알림 시간 설정 했을 경우
// https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/ (DatePickerDialog)
class MemoAlarmTimeSettingDialogFragment : DialogFragment() {
    var cal = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_memo_alarm_timesetting_dialog_fragment, container, false)
        val text_alarm_date = view.text_alarm_date
        val text_alarm_time = view.text_alarm_time
        text_alarm_date.text = SimpleDateFormat("yyyy. MM. dd (E)", Locale.KOREA).format(cal.time)
        text_alarm_time.text = SimpleDateFormat("a hh:mm", Locale.KOREA).format(cal.time)

        // 날짜 선택 했을 때의 Listener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                text_alarm_date.text = SimpleDateFormat("yyyy. MM. dd (E)", Locale.KOREA).format(cal.time)
            }
        }

        val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)

                text_alarm_time.text = SimpleDateFormat("a hh:mm", Locale.KOREA).format(cal.time)
            }
        }


        // 날짜를 선택했을 때 DatePicker 보여줌
        view.text_alarm_date.setOnClickListener {
            DatePickerDialog(this.context as Context, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 시간을 선택했을 때 TimePicker 보여줌
        view.text_alarm_time.setOnClickListener() {
            TimePickerDialog(this.context as Context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false).show()
        }

        // 취소버튼. 메인으로 돌아감
        view.button_cancel_setting.setOnClickListener(){
            dismiss()
        }

        // 확인버튼. 지정된 시간에 Notification 뜨도록 설정함
        view.button_ok_setting.setOnClickListener() {

        }

        return view
    }

}
