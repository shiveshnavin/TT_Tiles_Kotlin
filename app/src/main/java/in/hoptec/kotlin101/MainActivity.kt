package `in`.hoptec.kotlin101

import `in`.hoptec.kotlin101.utils.GenricCallback
import `in`.hoptec.kotlin101.utl.ClickCallBack
import `in`.hoptec.kotlin101.utl.Companion.TYPE_DEF
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
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Double.parseDouble
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.util.ArrayList
import android.R.menu
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.MenuInflater
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast

import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics


class MainActivity : AppCompatActivity() {




    lateinit var music:MediaPlayer
    lateinit var hit:MediaPlayer
    lateinit var moo:MediaPlayer

    val c0=0
    var refx= ArrayList<Float>()
    var score_i=0

    var LV1=50

    var LV2=100
    var LV3=175
    var LV4=250
    var LIFE=3



     var curuser : FirebaseUser? =null

    lateinit var mFirebaseAnalytics : FirebaseAnalytics;



    lateinit var ctx : Context
    lateinit var act : Activity
    var rxn =0f

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.getCurrentUser()

        if(currentUser!=null)
        {
            curuser=currentUser



            utl.l("Current user : "+curuser?.displayName)

            if(currentUser.displayName==null)
            {




                if(utl.getKey("user",ctx)!=null)
                {
                    currentUser.updateEmail(utl.refineString(utl.getKey("user",ctx),"_")+"@taptap.com");

                }
                else{




                 var cb: utl.InputDialogCallback = object : utl.InputDialogCallback {
                    override fun onDone(text: String) {


                        utl.setKey("user",text,ctx)
                        currentUser.updateEmail(utl.refineString(text,"_")+"@taptap.com");


                    }
                }


                utl.inputDialog(ctx,"Enter Your Name","",TYPE_DEF,cb);

                }

            }
        }
        else
        {
            utl.showDig(true,ctx)
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        utl.showDig(false,ctx)



                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                           utl.l( "signInAnonymously:success")
                            val user = mAuth.currentUser

                            curuser=user


                            var cb: utl.InputDialogCallback = object : utl.InputDialogCallback {
                                override fun onDone(text: String) {

                                    user?.updateEmail(utl.refineString(text,"_")+"@taptap.com");


                                    utl.setKey("user",text,ctx)

                                }
                            }


                            utl.inputDialog(ctx,"Enter Your Name","",TYPE_DEF,cb);





                        } else {
                            // If sign in fails, display a message to the user.
                            utl.l( "signInAnonymously:failure" )


                        }

                        // ...
                    }
        }

    }

    lateinit var mAuth :  FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utl.fullScreen(this)
        ctx=this
        act=this
        AndroidNetworking.initialize(this)
        setContentView(R.layout.activity_main)

        Constants.init(this)
        utl.init(ctx)

        FirebaseApp.initializeApp(this)

        mAuth= FirebaseAuth.getInstance();


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);




        var cb: utl.ClickCallBack = object : utl.ClickCallBack {
            override fun done(dialogInterface: DialogInterface) {


                var int= Intent(Intent.ACTION_VIEW)
                int.setData(Uri.parse("http://thehoproject.co.nf/terminal.php?app=tttiles"))
                startActivity(int)






            }



        }

        AndroidNetworking.get("http://thehoproject.co.nf/status.php?q=tttiles&u="+curuser?.displayName).build().getAsString(object : StringRequestListener {
            override fun onResponse(response: String) {


                utl.l(""+response)
                if(response.contains("cool"))
                {
                    utl.diag(ctx,"Update Required !","Something awesome has been added . Feel IT ! update NOW !",false,"UPDATE",cb)
                }


            }

            override fun onError(ANError: ANError) {

            }
        })


       // utl.setShared(ctx)




        setSupportActionBar(toolbar)

        name.setOnLongClickListener {

             LV1=10
             LV2=20
             LV3=30
             LV4=40
             LIFE=20

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


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.getItemId()) {

            R.id.setting -> {



                utl.snack(act,"Settings coming soon !")

                return true
            }

            R.id.board -> {

                utl.snack(act,"Leatherboards coming soon !")

                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        return true

    }

    var tile_list = ArrayList<Button>()


    var sqr=3

    var MUSIC_STARTED=false
    fun addtiles()
    {


        if(!MUSIC_STARTED)
        startMusic()



        tiles.visibility=VISIBLE
        gameover.visibility= GONE

         if(tile_list.size>=0)
        {
            var i=0

            while(i<tile_list.size)
            {
                utl.l(" NEXT I TAG WAS: "+tile_list.get(i).tag)
                tile_list.get(i).tag="OFF"
                utl.l(" NEXT I TAG NOW : "+tile_list.get(i).tag)

                i++
            }

        }


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


        if(tile_list.size>=0)
        {
            var i=0

            while(i<tile_list.size)
            {
                tile_list.get(i).tag="ON"
                utl.l(" NEXT I TAG IS: "+tile_list.get(i).tag)
                i++
            }

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
     override fun onLife(lie: Any) {



         playSound(3)


         life.setText(lie?.toString())
         //        YoYo.with(Techniques.Tada).duration(200).playOn(btn)



     }

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

            var sco:Float= parseFloat(scor2.toString())
           var scorz=(1.0f-roundoff(avg,3))*(sco)*10.0f

            scor= scorz.toInt()
//          finals.setText("Score : "+scor2.toString()+"\nAvg.  Reflex : "+roundoff(avg,3)+"  s")

            finals.setText("Score : "+scor+"\nAvg.  Reflex : "+roundoff(avg,3)+"  s")


            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, curuser?.uid)
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,  curuser?.email)
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "score")
            bundle.putString(FirebaseAnalytics.Param.CONTENT, "Score : "+scor+"\nAvg.  Reflex : "+roundoff(avg,3)+"  s")


            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)




        }



     var scor : Int= 0
        override fun onDo(obj: Any, obj2: Any) {

        }

        override fun onUpdate(scor: Any) {

            hit()

            score.setText("Hits : "+scor.toString())
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
        game.life=LIFE
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


        MUSIC_STARTED=true
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

        MUSIC_STARTED=false

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

            sm = IntArray(4)
            // fill your sounds
            sm[0] = soundPool!!.load(mContext, R.raw.razor_l, 1)
            sm[1] = soundPool!!.load(mContext, R.raw.tap_h, 1)
            sm[2] = soundPool!!.load(mContext, R.raw.moo, 1)
            sm[3] = soundPool!!.load(mContext, R.raw.beep9, 1)

            amg = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }

         fun playSound(sound: Int) {

             try {
                 soundPool!!.play(sm!![sound], 1f, 1f, 1, 0, 1f)
             } catch(e: Exception) {
             }
         }



}
