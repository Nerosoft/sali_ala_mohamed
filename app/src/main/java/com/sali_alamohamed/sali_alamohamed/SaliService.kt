package com.sali_alamohamed.sali_alamohamed


import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.media.AudioManager
import android.net.Uri



class SaliService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (am.ringerMode) {
            AudioManager.RINGER_MODE_SILENT  ->   showNotification(753)   //Log.i("MyApp", "Silent mode")
            AudioManager.RINGER_MODE_VIBRATE ->   showNotification(753)  //Log.i("MyApp", "Vibrate mode")
            AudioManager.RINGER_MODE_NORMAL  ->   showNotification(159) //Log.i("MyApp", "Normal mode")
        }
        AlarmReceiver.keepRunning(this)
        return START_NOT_STICKY
    }


    private fun callSound(ringerMode: Int): Int {
        if(ringerMode==753)
            return Notification.DEFAULT_VIBRATE

        val mode = this.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("mode", 0)
        when (mode) {
            0 ->
                return R.raw.sound
            1 ->
                return R.raw.sound2
            2 ->
                return R.raw.sound3
            3 ->
                return R.raw.sound4
            4 ->
                return R.raw.sound5 //استغفر الله
            5 ->
                return R.raw.sound6 // سبحان الله
            6 ->
                return R.raw.sound7 //الحمد لله
            7 ->
                return R.raw.sound8 // لا الاه الا الله
            8 ->
                return R.raw.sound9 // الله اكبر
            9 ->
                return R.raw.sound10 // لا حول ولا قوة الا بالله
            10 ->
                return R.raw.sound11 //اللهم صلي علي محمد
            11 ->
                return R.raw.sound12 // سبحان الله وبحمده سبحان الله العظيم
            12 ->
                return R.raw.sound13 // استغفر الله واتوب اليه
            13 ->
                return R.raw.sound14 // الحمد لله رب العالمين
            14 ->
                return R.raw.sound15 // سبحان الله وبحمده
            15 ->
                return R.raw.sound16 // ماهر زين صلي
            else ->
                return R.raw.sound


        }
    }


    private fun showNotification(ringerMode: Int) {

        val icon = BitmapFactory.decodeResource(resources,
                R.mipmap.ic_launcher)

        val notification = NotificationCompat.Builder(this, "101")
                .setContentTitle("صلي علي الحبيب")
                .setTicker("صلي علي الحبيب")
                .setContentText("عليه افضل الصلاة والسلام")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .setAutoCancel(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.FLAG_FOREGROUND_SERVICE)
                .setSound(Uri.parse("android.resource://"
                        + this.packageName + "/" + callSound(ringerMode)))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build()

        startForeground(101,
                notification)

    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}
