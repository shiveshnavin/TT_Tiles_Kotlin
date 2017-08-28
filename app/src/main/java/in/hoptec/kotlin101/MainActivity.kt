package `in`.hoptec.kotlin101

import `in`.hoptec.kotlin101.utils.GenricCallback
import android.app.Activity
import android.content.Context
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



    lateinit var ctx : Context
    lateinit var act : Activity
    var rxn =0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utl.fullScreen(this)
        ctx=this
        act=this
        setContentView(R.layout.activity_main)


        activity_main.setBackgroundColor(R.color.cart_back)


        addtiles()

        restart.setOnClickListener {

            addtiles()

        }





    }



    var tile_list = ArrayList<Button>()


    var sqr=3

    fun addtiles()
    {



        tiles.visibility=VISIBLE
        finals.visibility= VISIBLE



        tiles.removeAllViews()

        var i : Int  =0
        tiles.weightSum=9f

        while(i++<sqr)
        {

            var j : Int  =0
            val row=LinearLayout(ctx)
            var params=LinearLayout.LayoutParams(utl.getW(ctx),0)

            params.gravity=LinearLayout.VERTICAL
            row.gravity=Gravity.CENTER
            params.weight=3f
            row.weightSum=9f
            row.layoutParams=params

                    while(j++<sqr)
                    {

                        var params=LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT)

                        params.gravity=LinearLayout.VERTICAL

                        params.weight=3f



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

        utl.snack(act,"Tap first tile to start !")


    }

    fun startGame()
    {
        utl.l("Starting Game")

       val call_back = object : GenricCallback {
           override fun onRxn(reaction: Any) {

               kotlin.setText("Your Reflex : "+parseFloat(reaction.toString())/1000+" s")
               rxn=rxn+parseFloat(reaction.toString())/1000

           }

           override fun onStart() {

        }

        override fun onGameEnd(scor2: Any) {

            score.setText("Score : "+scor2.toString())

            tiles.visibility=GONE
            gameover.visibility= VISIBLE
            finals.setText("Score : "+scor2.toString()+"\nAvg Reflex : "+(rxn/ parseInt(scor2.toString())))



        }

        override fun onDo(obj: Any, obj2: Any) {

        }

        override fun onUpdate(scor: Any) {


            score.setText("Score : "+scor.toString())


        }



    }



        val game : Game =Game(tile_list,call_back)
        game.start()


        utl.animateBackGround(activity_main,"#FF5722","#00E099",true,10000)

    }





}
