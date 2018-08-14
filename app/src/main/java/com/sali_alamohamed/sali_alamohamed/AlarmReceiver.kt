package com.sali_alamohamed.sali_alamohamed


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver

import android.content.Context
import android.content.Intent

import android.os.Build
import android.os.PowerManager

import android.support.v4.content.ContextCompat;

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        runPower(context)
        var myService = Intent(context, SaliService::class.java)
        // context.startService(myService)
        ContextCompat.startForegroundService(context, myService);
    }


    lateinit var pm: PowerManager
    lateinit var wl: PowerManager.WakeLock
    fun runPower(context: Context) {
        this.pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE, "TAG")
        //   wl.setReferenceCounted(true)
        wl.acquire()
    }

    fun stopPower() {
        if (wl != null) wl.release()
    }


    companion object {
        fun keepRunning(context: Context) {
            val time = context.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("SPPROG", 5)
            var myAlarm: Intent
            var recurringAlarm: PendingIntent? = null
            var alarms: AlarmManager
            myAlarm = Intent(context, AlarmReceiver::class.java)
            //myAlarm.putExtra("project_id", project_id); //Put Extra if needed
            recurringAlarm = PendingIntent.getBroadcast(context, 0, myAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
            alarms = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val ac = AlarmManager.AlarmClockInfo(System.currentTimeMillis() + (1000 * 60 * time),
                        recurringAlarm)
                alarms.setAlarmClock(ac, recurringAlarm)
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
                alarms.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * time), recurringAlarm)
            else
                alarms.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * time), recurringAlarm)

        }
    }

}