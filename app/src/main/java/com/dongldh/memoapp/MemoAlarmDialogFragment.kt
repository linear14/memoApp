package com.dongldh.memoapp

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
            val bundle = Bundle()
            bundle.putSerializable("memoVO", memoVO)

            val timeSettingDialogFragment = MemoAlarmTimeSettingDialogFragment()
            timeSettingDialogFragment.arguments = bundle
            timeSettingDialogFragment.show(
                activity?.supportFragmentManager as FragmentManager, "dialog_event"
            )
            dismiss()
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
    lateinit var memoVO: DataItemMemo
    var cal = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_memo_alarm_timesetting_dialog_fragment, container, false)
        memoVO = (arguments?.getSerializable("memoVO")) as DataItemMemo
        val text_alarm_date = view.text_alarm_date
        val text_alarm_time = view.text_alarm_time
        text_alarm_date.text = SimpleDateFormat("yyyy. MM. dd (E)", Locale.KOREA).format(cal.time)
        text_alarm_time.text = SimpleDateFormat("a hh:mm", Locale.KOREA).format(cal.time)

        // 날짜 선택 했을 때의 Listener
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                text_alarm_date.text = SimpleDateFormat("yyyy. MM. dd (E)", Locale.KOREA).format(cal.time)
            }

        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { view , hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                cal.set(Calendar.SECOND, 0)

                text_alarm_time.text = SimpleDateFormat("a hh:mm", Locale.KOREA).format(cal.time)
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
        // https://kwongyo.tistory.com/5
        view.button_ok_setting.setOnClickListener() {
            dismiss()
            // 이것도 마찬가지로 그냥 getSystemService를 사용하면 안됨 (getSystemService는 Context내의 메서드라서 context를 불러줘야됨(Fragment는 Context를 상속 안받음). 따라서 Fragment 내에서의 사용은 아래와 같음)
            val am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this.context, BroadCastAlarm::class.java)
            intent.putExtra("num", memoVO.num)
            intent.putExtra("title", memoVO.title)
            intent.putExtra("content", memoVO.content)
            val sender = PendingIntent.getBroadcast(this.context, memoVO.num, intent, 0)

            val selectedTime = cal.timeInMillis

            // log. 입력 된 시간 (문제 없음)
            // Toast.makeText(context, SimpleDateFormat("hh : mm : ss").format(selectedTime), Toast.LENGTH_SHORT).show()

            am.set(AlarmManager.RTC_WAKEUP, selectedTime, sender)
        }

        return view
    }
}

// 문제점 두 가지
// 1. 알림 설정을 두개의 항목에다가 각각 설정하면, update가 되어서, 마지막에 설정한 알림이 작동한다. --> pendingIntent.getActivity의 두번쨰 param를 memoVO.num으로 제각기 다르게 설정으로 해결가능성)
// 2. 딱 0초에 실행되는 것이 아니라.. 제 각각이다. --> setWhen이 의심스러움.. 이따가 확인
class BroadCastAlarm: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // log
        // Toast.makeText(context, SimpleDateFormat("hh : mm : ss").format(System.currentTimeMillis()), Toast.LENGTH_LONG).show()

        // 방아올 때, data class 자체를 객체로 받아오는 것이 아니라, 각각의 필요한 데이터를 intent에서 넘겨주어야 함.
        // https://stackoverflow.com/questions/39209579/how-to-pass-custom-serializable-object-to-broadcastreceiver-via-pendingintent 참고
        val num = intent.getIntExtra("num", 0)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(context, num, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, "default")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.color = Color.BLUE
        builder.setContentIntent(pendingIntent)
        builder.setWhen(System.currentTimeMillis())
        builder.setAutoCancel(true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT))
        }
        notificationManager.notify(2, builder.build())
    }

}
