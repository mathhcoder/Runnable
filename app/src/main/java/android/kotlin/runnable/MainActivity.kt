package android.kotlin.runnable

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


const val NUM = "number"
const val ISRUNNING = "isRunning"
const val LASTTIME = "lastTime"
const val STOP = "stop"
const val RUNNING = "running"
const val RESET = "reset"


class MainActivity : AppCompatActivity() {

    var isRunnning = STOP
    var num = 0
    var hander = Handler()
    var runnable = Runnable {}

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sh = getSharedPreferences(
            "MySharedPref",
            Context.MODE_APPEND)

        isRunnning = sh.getString(ISRUNNING , RESET).toString()
        if(isRunnning != RESET ){
            num = sh.getInt(NUM , 0)
            var lastTime = sh.getLong(LASTTIME , 0)
            var time = Calendar.getInstance()
            textView.setText("Time : ${num}")
            if(isRunnning == RUNNING) {
                num += ((time.timeInMillis - lastTime) / 1000).toInt()
                textView.setText("Time : ${num}")
                start()
            }
        }
        start.setOnClickListener({start()})
        stop.setOnClickListener({ stop() })
        reset.setOnClickListener({reset()})
    }


    override fun onDestroy() {
        super.onDestroy()
        var sharedPreferences = getSharedPreferences(
            "MySharedPref",
            Context.MODE_PRIVATE
        )
        val myEdit = sharedPreferences.edit()
        myEdit.putInt(NUM, num)
        var curTime = Calendar.getInstance()
        myEdit.putLong(LASTTIME , curTime.timeInMillis)
        myEdit.putString(ISRUNNING , isRunnning)
        myEdit.commit()

        Log.e("jama" , "onDestroy ${textView.text}")
    }

    fun start(){
        runnable = object : Runnable{
            override fun run() {
                hander.postDelayed(this, 1000)
                num ++
                textView.text = "Time : " + num.toString()
            }

        }
        hander.post(runnable)
        isRunnning = RUNNING
    }

    fun stop(){
        Log.e("jama" , "stop")
        hander.removeCallbacks(runnable)
        textView.text = "Time : " + num
        isRunnning = STOP
    }

    fun reset(){
        Log.e("jama" , "reset")
        hander.removeCallbacks(runnable)
        num = 0
        textView.text = "Time : " + num
        isRunnning = RESET
    }


}