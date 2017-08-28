package `in`.hoptec.kotlin101

import `in`.hoptec.kotlin101.utils.GenricCallback
import android.os.Handler
import android.widget.Button
import java.util.*

/**
 * Created by shivesh on 28/8/17.
 */

class Game(internal var buttons: ArrayList<Button>,internal var callback:GenricCallback) {


    var score : Int =0
    val RUNNING =12
    val ENDED=13
    val PAUSED=14
    var STATE=ENDED

    var duration:Long=1000

    lateinit var run:Runnable
    lateinit var h:Handler

    fun disableall() {


        var i=buttons.size-1
        while(i-->=0)
            disable1(i)

    }


    fun disable1(n: Int) {

        buttons.get(n).setBackgroundColor(R.color.grey_400)
        buttons.get(n).setOnClickListener {

           if(STATE== RUNNING)
           {
               end()
           }

        }

    }


    fun enable1(n: Int) {


    }

    fun start()
    {


        STATE=RUNNING
        disableall()
        h=Handler()
        run= Runnable {


            val random:Random = Random()
            val x=random.nextInt(buttons.size)
            duration-=30
            h.postDelayed(run,duration)

        }



    }

    fun end()
    {
        STATE=ENDED

        disableall()

    }



    fun pause()
    {
        STATE=ENDED

    }


}
