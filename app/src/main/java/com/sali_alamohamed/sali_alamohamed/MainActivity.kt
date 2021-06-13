package com.sali_alamohamed.sali_alamohamed


import android.app.ActivityManager
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


class MainActivity : AppCompatActivity() {

    lateinit var myAlarm: Intent
    var recurringAlarm: PendingIntent? = null
    lateinit var alarms: AlarmManager
    lateinit var textCount: TextView
    lateinit var ButImg: ImageView
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButImg = findViewById(R.id.imageViewButton)
        textCount = findViewById(R.id.textView2)
        spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        refreshSetting()

        ButImg.setOnClickListener {

            if (ButImg.tag.toString() == "off") {
                var setTime = dialogTime()
                setTime.listener = View.OnClickListener {
                    refreshSetting()
                    runAlarm()
                }
                setTime.show(supportFragmentManager, "")
            } else {
                canselAlert()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applicationContext.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).edit()
                        .putInt("mode", position).commit()
            }

        }
    }


    fun refreshSetting() {
        val time = this.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("SPPROG", 5)
        spinner.setSelection(this.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("mode", 0))
        textCount.text = "Every : " + time + " Minute"


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActiveNotification(101)) {
                ButImg.setImageResource(R.drawable.icon_btn_on)
                ButImg.tag = "on"
            }

        } else
            if (isMyServiceRunning(SaliService::class.java)) {
                ButImg.setImageResource(R.drawable.icon_btn_on)
                ButImg.tag = "on"
            }

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun runAlarm() {

        val receiver = ComponentName(this, AlarmReceiver::class.java)
        val pm = this.packageManager
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)

        AlarmReceiver.keepRunning(applicationContext)

        var myService = Intent(applicationContext, SaliService::class.java)
        ContextCompat.startForegroundService(applicationContext, myService);

        ButImg.setImageResource(R.drawable.icon_btn_on)
        ButImg.tag = "on"
    }

    fun canselAlert() {
        val receiver = ComponentName(this, AlarmReceiver::class.java)
        val pm = this.packageManager
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)

        myAlarm = Intent(applicationContext, AlarmReceiver::class.java)
        //myAlarm.putExtra("project_id",project_id); //put the SAME extras
        recurringAlarm = PendingIntent.getBroadcast(applicationContext, 0, myAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
        alarms = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarms.cancel(recurringAlarm)


        stopService(Intent(this, SaliService::class.java))
        ButImg.setImageResource(R.drawable.ic_icon_btn_off)
        ButImg.tag = "off"
    }

    fun clickTO(view: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Sali-ala-mohamed-107350450790911/"))
        startActivity(browserIntent)
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getActiveNotification(notificationId: Int): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val barNotifications = notificationManager.activeNotifications
        for (notification in barNotifications) {
            if (notification.id == notificationId) {
                return true
                //return notification.notification
            }
        }

        return false
    }

    @Suppress("deprecation")
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                //   Log.i("isMyServiceRunning?", true.toString() + "")
                return true
            }
        }
        // Log.i("isMyServiceRunning?", false.toString() + "")
        return false
    }

}
