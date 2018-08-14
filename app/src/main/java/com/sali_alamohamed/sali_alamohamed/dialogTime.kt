package com.sali_alamohamed.sali_alamohamed

import android.app.Dialog
import android.content.Context
import android.app.DialogFragment
import android.app.AlertDialog
import android.view.View
import android.widget.TextView
import android.widget.SeekBar
import android.widget.Toast
import android.content.DialogInterface
import android.view.LayoutInflater
import android.os.Bundle
import android.widget.Button


class dialogTime : DialogFragment() {

    lateinit var seekBar: SeekBar
    lateinit var layView: View
    lateinit var builder: AlertDialog.Builder
    lateinit var textView: TextView
    lateinit var listener:View.OnClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        var inflater: LayoutInflater = activity.layoutInflater
        builder.setTitle("ادخل الوقت المناسب لك للذكر")
        layView = inflater.inflate(R.layout.lay_set_time, null)

        builder.setView(layView).setPositiveButton("OK", { dialogInterface: DialogInterface, id: Int ->

            activity.applicationContext.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).edit()
                    .putInt("SPPROG",
                            if(seekBar.progress==0) 1
                            else seekBar.progress).commit()
            listener.onClick(null)
        }).setNegativeButton("Cancel", { dialogInterface: DialogInterface, id: Int ->
            dialogInterface.cancel()
        })

        textView = layView.findViewById(R.id.textViewtime)
        seekBar = layView.findViewById(R.id.seekBar5)

        if (activity.applicationContext.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("SPPROG", 5) == 5) {
            seekBar.progress = 5
        } else
            seekBar.progress = activity.applicationContext.getSharedPreferences("mySpinner", Context.MODE_PRIVATE).getInt("SPPROG", 5)

        textView.text = "" + seekBar.progress

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textView.text = Integer.toString(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                testprog()
            }
        })


        layView.findViewById<Button>(R.id.buttonM).setOnClickListener({
            seekBar.progress -= 1
            Toast.makeText(activity.applicationContext, "احسنت كلما قل كثر الذكر", Toast.LENGTH_SHORT).show();
            testprog()
        })

        layView.findViewById<Button>(R.id.buttonP).setOnClickListener({
            seekBar.progress += 1
        })

        return builder.create()
    }


    private fun testprog() {
        if (seekBar.progress < 5)
            seekBar.progress = 5
    }

}