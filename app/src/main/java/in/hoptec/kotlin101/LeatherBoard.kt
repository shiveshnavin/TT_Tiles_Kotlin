package `in`.hoptec.kotlin101

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.daniribalbert.customfontlib.views.CustomFontTextView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_leatherboard.*
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Double
import java.util.ArrayList
import java.util.stream.Collectors

class LeatherBoard : AppCompatActivity() {

    lateinit var scores:DatabaseReference

    var setOnce=false
    lateinit var score_list:ArrayList<Score?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utl.fullScreen(this)
        setContentView(R.layout.activity_leatherboard)
        val iit = Intent(this, MainActivity::class.java)

        scores = FirebaseDatabase.getInstance(Constants.fireURL()).getReference("TapTapTiles").child("scores")


        scores.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var i=0


                    score_list = ArrayList<Score?>()

                    for (dc in dataSnapshot.children) {

                        val scr = dc.getValue(Score::class.java)

                        score_list.add(scr)


                    }

                    setUpScores(score_list)
                }



            override fun onCancelled(databaseError: DatabaseError) {

            }
        })




    }
    var useRecycler=true
    fun setUpScores(scrs:ArrayList<Score?> )
    {
        var sortedList = ArrayList(scrs.sortedWith(compareBy({ it?.score })))

        for (obj in sortedList) {
            println(obj?.score)
        }

         sortedList.reverse()


        var adap=GRecyclerAdapter(this,process(sortedList) )
        listr.layoutManager=LinearLayoutManager(this)
        listr.adapter=adap



        if(useRecycler)
            return;




        list.removeAllViews()
        var i=1

        for(scr in sortedList)
        {
            try {
                var l:LinearLayout = layoutInflater.inflate(R.layout.row_score,list).findViewById(R.id.main) as LinearLayout

              //  utl.l(""+scr?.toJson())
             //   utl.l(l)

                var snom=(l.findViewById(R.id.snom) as CustomFontTextView )
              //  utl.l(snom)
                snom.text = ""+i

                (l.findViewById(R.id.name) as CustomFontTextView).text = scr?.name
                (l.findViewById(R.id.score) as CustomFontTextView ).text = ""+scr?.score
                (l.findViewById(R.id.reflex) as CustomFontTextView ).text = ""+scr?.reflex


                list.addView(l)
            } catch(e: Exception) {
            }


            i++
        }



    }


    fun sortDesc(list:ArrayList<Score?>): ArrayList<Score?>
    {

        var sortedList = ArrayList(list.sortedWith(compareBy({ it?.score })))

        for (obj in sortedList) {
            println(obj?.score)
        }


        return sortedList
    }



    fun process(list:ArrayList<Score?>): ArrayList<Score?>
    {
        var ret  =ArrayList<Score?>()

        for(obj in list)
        {
            var name=obj?.name

            for(r in ret)
            {
                if( r?.name.equals(name))
                    break;
            }

            var retn  =ArrayList<Score?>()

            for(onji in list)
            {
                if(onji?.name.equals(name)){
                    retn.add(onji)
                }
            }
            var sort_onji=sortDesc(retn)
            ret.add(sort_onji.get(sort_onji.size-1))

        }



        var lst=ArrayList<Score?>(sortDesc(ret).reversed())


        val l1 = ArrayList<Score?>(lst)
        val l2 = ArrayList<Score?>()

        val iterator = l1.iterator()

        while (iterator.hasNext()) {
            val o = iterator.next() as Score
            if (!l2.contains(o)) l2.add(o)
        }

        bkp=l2
        if(!setOnce)
        {
            setOnce=true
            scores.removeValue()
            for(sc2r in l2)
            {
                scores.push().setValue(sc2r)

            }
        }



        return l2
    }


    lateinit var bkp : ArrayList<Score?>
}
