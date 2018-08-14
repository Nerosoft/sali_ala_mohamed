package com.sali_alamohamed.sali_alamohamed

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context

import android.media.MediaPlayer



class JopService: JobService(){

    companion object {
        var apk:Boolean = true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        callSound(applicationContext)
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    fun callSound(context: Context) {
        val mode = context.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("mode", 0)
        when (mode) {
            0 ->
                MediaPlayer.create(context, R.raw.sound).start()
            1 ->
                MediaPlayer.create(context, R.raw.sound2).start()
            2 ->
                MediaPlayer.create(context, R.raw.sound3).start()
            3 ->
                MediaPlayer.create(context, R.raw.sound4).start()

            else -> MediaPlayer.create(context, R.raw.sound).start()
        }

    }

}


