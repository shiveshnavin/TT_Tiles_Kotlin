package `in`.hoptec.kotlin101

import `in`.hoptec.kotlin101.utils.GenricCallback
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Double.parseDouble
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.util.ArrayList

class MainActivity : AppCompatActivity() {



    lateinit var music:MediaPlayer
    lateinit var hit:MediaPlayer
    lateinit var moo:MediaPlayer

    var refx= ArrayList<Float>()
    var score_i=0

    var LV1=50
    var LV2=100
    var LV3=175
    var LV4=250

    lateinit var ctx : Context
    lateinit var act : Activity
    var rxn =0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utl.fullScreen(this)
        ctx=this
        act=this
        setContentView(R.layout.activity_main)

        name.setOnLongClickListener {

             LV1=10
             LV2=20
             LV3=30
             LV4=40

            false

        }



        moo= MediaPlayer.create(ctx,R.raw.moo)

        music= MediaPlayer.create(ctx,R.raw.razor_l)
        music.seekTo(2000)
        music.setOnPreparedListener {


        }

        hit= MediaPlayer.create(ctx,R.raw.tap_h)

        InitSound()


        activity_main.setBackgroundColor(R.color.cart_back)

//        utl.changeColorDrawable(restart,R.color.white)

        addtiles()

        restart.setOnClickListener {

            hit()
            if(moo.isPlaying) {
                moo.seekTo(0)
              moo.pause()
            }
            addtiles()

        }


//        music.prepare()


      //  moo.prepare()
       // hit.prepare()


    }



    var tile_list = ArrayList<Button>()


    var sqr=3

    fun addtiles()
    {


        startMusic()



        tiles.visibility=VISIBLE
        gameover.visibility= GONE



        tiles.removeAllViews()

        tile_list= ArrayList<Button>()
        refx= ArrayList<Float>()

        var i : Int  =0
        tiles.weightSum=sqr.toFloat()*sqr.toFloat()

        while(i++<sqr)
        {

            var j : Int  =0
            val row=LinearLayout(ctx)
            var params=LinearLayout.LayoutParams(utl.getW(ctx),0)

            params.gravity=LinearLayout.VERTICAL
            row.gravity=Gravity.CENTER
            params.weight=sqr.toFloat()
            row.weightSum=sqr.toFloat()*sqr.toFloat()
            row.layoutParams=params

                    while(j++<sqr)
                    {

                        var params=LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT)

                        params.gravity=LinearLayout.VERTICAL

                        params.weight=sqr.toFloat()



                        val button=Button(ContextThemeWrapper(ctx,R.style.Button))
                        button.text=""
                        button.layoutParams=params

                        button.setBackgroundResource(R.drawable.tile_bg)
                        row.addView(button)
                        utl.l("Adding tile No : R->"+i+" C->"+j)

                        tile_list.add(button)



                    }
            tiles.addView(row)

        }

        tile_list[0].setBackgroundResource(R.drawable.tile_bg_on)
        tile_list[0].setOnClickListener {

            utl.l("Tile Onclick 104")

            tile_list[0].setOnClickListener {  }
            startGame()
        }

       // utl.snack(act,"Tap first tile to start !")


    }

    lateinit var game : Game
    fun startGame()
    {
        utl.l("Starting Game")
 val call_back = object : GenricCallback {
           override fun onRxn(reaction: Any) {

               kotlin.setText("Your  Reflex : "+parseFloat(reaction.toString())/1000+"   s")
               rxn=rxn+parseFloat(reaction.toString())/1000


               refx.add(parseFloat(reaction.toString())/1000)
           }

           override fun onStart() {

        }

        override fun onGameEnd(scor2: Any) {

            endMusic()
            laugh()
            score.setText("Score : "+scor2.toString())

            score_i=0
            sqr=3
            tiles.visibility=GONE
            gameover.visibility= VISIBLE
            var ttl=0f

            for(ref in refx)
            {
                ttl=ttl+ref
            }
            var avg=ttl/refx.size

            //finals.setText("Score : "+scor2.toString()+"\nAvg.  Reflex : "+roundoff(rxn/ (parseInt(scor2.toString())+1),3)+"  s")

            finals.setText("Score : "+scor2.toString()+"\nAvg.  Reflex : "+roundoff(avg,3)+"  s")



        }

        override fun onDo(obj: Any, obj2: Any) {

        }

        override fun onUpdate(scor: Any) {

            hit()

            score.setText("Score : "+scor.toString())
            score_i=parseInt(scor.toString())

            if(score_i==LV1) {
                sqr++
                addtiles()

            }

            if(score_i==LV2) {
                sqr++
                addtiles()

            }


            if(score_i==LV3) {
                sqr++
                addtiles()

            }


            if(score_i==LV4) {
                sqr++
                addtiles()

            }
        }



    }



        game =Game(ctx,tile_list,call_back)
        if(sqr>3)
        {
            game.score=score_i
        }
        game.start()


        utl.animateBackGround(activity_main,"#FF5722","#0a8f08",true,10000)

    }


    fun roundoff( floa : Float , n:Int) : Float
    {
        val it:Float =parseFloat(""+(floa*(Math.pow(10.0, parseDouble(""+n)))))

        val intt=it.toInt()

        return intt/Math.pow(10.0, parseDouble(""+n)).toFloat();
    }


    fun startMusic()
    {

        try {


            music.isLooping=true


            if(moo.isPlaying) {


                moo.seekTo(0)
                moo.pause()

            }
            music.start()
        } catch(e: Exception) {
        }


    }
    fun endMusic()
    {



        try {


            if(music.isPlaying) {
                music.seekTo(2000)
                music.pause()

            }


        } catch(e: Exception) {
        }

    }

    fun hit()
    {


        try {
            playSound(1)
           /* if(hit.isPlaying)
                hit.stop()
            hit.start()

*/
        } catch(e: Exception) {
        }

    }


    fun laugh()
    {

        try {
            moo.start()
        } catch(e: Exception) {
            moo.reset()
            moo= MediaPlayer.create(ctx,R.raw.moo)
        }

    }

    override fun onDestroy() {

        try {
            moo.stop()
        } catch(e: Exception) {
        }
        try {
            music.stop()
        } catch(e: Exception) {
        }
        try {cleanUpIfEnd()
            hit.stop()
        } catch(e: Exception) {
        }
        super.onDestroy()
    }



    fun cleanUpIfEnd() {

        soundPool!!.release()
        soundPool = null
    }



         var soundPool: SoundPool? = null
        lateinit  var sm: IntArray
        lateinit  var amg: AudioManager


        fun InitSound() {

            val maxStreams = 1
            val mContext : Context =ctx
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                soundPool = SoundPool.Builder()
                        .setMaxStreams(maxStreams)
                        .build()
            } else {
                soundPool = SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0)
            }

            sm = IntArray(3)
            // fill your sounds
            sm[0] = soundPool!!.load(mContext, R.raw.razor_l, 1)
            sm[1] = soundPool!!.load(mContext, R.raw.tap_h, 1)
            sm[2] = soundPool!!.load(mContext, R.raw.moo, 1)

            amg = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }

         fun playSound(sound: Int) {

            soundPool!!.play(sm!![sound], 1f, 1f, 1, 0, 1f)
        }



}
