package com.sali_alamohamed.sali_alamohamed

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import android.content.pm.PackageManager
import android.content.ComponentName
import android.app.job.JobScheduler
import android.app.job.JobInfo







class MainActivity : AppCompatActivity() {

    lateinit var myAlarm: Intent
    var recurringAlarm: PendingIntent? = null
    lateinit var alarms: AlarmManager
    lateinit var textCount: TextView
    lateinit var ButImg:ImageView
    lateinit var spinner:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButImg= findViewById(R.id.imageViewButton)
        textCount=findViewById(R.id.textView2)
        spinner=findViewById(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        refreshSetting()


//        var alarmup: Boolean = PendingIntent.getBroadcast(this, 0,
//                Intent(this, AlarmReceiver::class.java),
//                PendingIntent.FLAG_NO_CREATE) != null
//
//        if (alarmup) {
//            ButImg.setImageResource(R.drawable.icon_btn_on)
//            ButImg.tag = "on"
//            refreshSetting()
//        }

        ButImg.setOnClickListener({

            if (ButImg.tag.toString() == "off") {
                var setTime = dialogTime()
                setTime.listener = View.OnClickListener {
                    refreshSetting()
                    runAlarm()
                   // runJop(ButImg)
                }
                setTime.show(fragmentManager, "")
            } else{
                canselAlert()
               // cancelJop(ButImg)
            }
        })

        spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applicationContext.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).edit()
                        .putInt("mode", position).commit()
            }

        }
    }



    fun refreshSetting(){
        val time = this.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("SPPROG", 5)
        spinner.setSelection( this.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("mode", 0))
        textCount.text = "Every : " + time + " Minute"
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

    //---------------------------------------------------


    fun runJop(ButImg: ImageView){


        // wrap your stuff in a componentName
        val receiver = ComponentName(this, JopService::class.java)
         val pm = this.packageManager
         pm.setComponentEnabledSetting(receiver,
                 PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)


    // set up conditions for the job
        val task = JobInfo.Builder(333, receiver)
             //   .setPeriodic(1)
                //.setOverrideDeadline(1)
                .setPeriodic(9000)
                .setRequiresDeviceIdle(false)
                //.setPeriodic((1000 * 60 *time).toLong())
                .setRequiresCharging(true) // default is "false"
               // .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // Parameter may be "ANY", "NONE" (=default) or "UNMETERED"
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // Parameter may be "ANY", "NONE" (=default) or "UNMETERED"
                .build()
    // inform the system of the job
        val jobScheduler = this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(task)
        ButImg.setImageResource(R.drawable.icon_btn_on)
        ButImg.tag = "on"
    }

    fun cancelJop(ButImg: ImageView){
        // Disable a receiver
        val receiver = ComponentName(this, JopService::class.java)
        val pm = this.packageManager
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
        val jobScheduler = this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancelAll()
        ButImg.setImageResource(R.drawable.ic_icon_btn_off)
        ButImg.tag = "off"
        JopService.apk=false
    }
//-----------------------------------------

    override fun onDestroy() {
        canselAlert()
        super.onDestroy()

    }

}
