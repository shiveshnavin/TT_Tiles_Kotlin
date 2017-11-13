package `in`.hoptec.kotlin101

import `in`.hoptec.kotlin101.utils.GenricCallback
import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.Button
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import java.util.*

/**
 * Created by shivesh on 28/8/17.
 */

class Game(internal var ctx:Context,internal var buttons: ArrayList<Button>,internal var callback:GenricCallback) {


    var is_mode_free=false
    var lastrxn=System.currentTimeMillis()
    var currxn=System.currentTimeMillis()
    var score : Int =0
    var fails = 0
    val RUNNING =12
    val ENDED=13
    val PAUSED=14
    var STATE=ENDED
    var life=3
    var clicked_m=false

    var duration:Long=1000

     lateinit var h:Handler

    fun disableall() {

        utl.l("Game disableAll")

        var i=buttons.size-1
        while(i>-1){


            disable1(i--)


        }

    }


    fun disable1(n: Int) {

        utl.l("Game disable1 "+n)
        val btn=buttons[n]

        btn.setBackgroundResource(R.drawable.tile_bg)

        btn.setOnClickListener {

           if(STATE== RUNNING)
           {
               end()
           }

        }
        YoYo.with(Techniques.Shake).duration(200).playOn(btn)


    }



    var clicked=false
    fun enable1(n: Int) {

        utl.l("Game enable1 "+n)



        val btn=buttons[n]
         var clicked=false

        if(!is_mode_free)
        btn.postDelayed( Runnable {


            utl.l("NEXT ON RUN")

            if(rerun&&!clicked&&btn.tag.equals("ON"))
            {
                next_fault()
            }
            clicked=true
            //--score;
            // next_fault()
            //duration-=30
            //  h.postDelayed(run,duration)

        },duration)


        btn.setBackgroundResource(R.drawable.tile_bg_on)
        btn.requestFocus()
        btn.setOnClickListener {




            clicked=true
            lastrxn=currxn
            currxn=System.currentTimeMillis()
            var diff=currxn-lastrxn
            callback.onRxn(diff)
            next()


        }
        YoYo.with(Techniques.Tada).duration(200).playOn(btn)

    }

    fun next()
    {

        if(duration>600)
         duration-=5

        rerun=true
       // h.postDelayed(run,duration)

        utl.l("Game Next " )
        disableall()
        enable1(ran())

        callback.onUpdate(++score)

    }


    fun next_fault()
    {

        utl.l("Game Next_Fault " )

        rerun=true

        disableall()
        enable1(ran())

        if(--life<0)
        {
            end()
        }
        if(life>-1)
          callback.onLife(life)


    }


    fun start()
    {


        life=3
        duration=1000
        STATE=RUNNING

        h=Handler()




        next()



        callback.onLife(life)


    }

    var rerun=true
    fun end()
    {
        rerun=false


        STATE=ENDED


        disableall()
        utl.l("On Game End")

        callback.onGameEnd(score)
    }



    fun pause()
    {
        STATE=ENDED

    }

    fun ran():Int{


        val random:Random = Random()
        val x=random.nextInt(buttons.size)

        utl.l("Game Random "+x)

        return x

    }

}
