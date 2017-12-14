package `in`.hoptec.kotlin101

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class GRecyclerAdapter(private val mContext: Context, private val feedItemList: List<Score?>?) : RecyclerView.Adapter<GRecyclerAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_score, null)

        val viewHolder = CustomViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {

        val item = feedItemList!![i]

        customViewHolder.snom.text = Html.fromHtml(""+(i+1))
        customViewHolder.score.text = ""+item?.score
        customViewHolder.reflex.text = ""+item?.reflex+"s"
        customViewHolder.name.text = ""+item?.name

        try {
            if(item?.name.equals(utl.getKey("scr_name",mContext)))
            {
                utl.animateBackGround(customViewHolder.base,"#ff0b8043","#ff0097a7",true,1000);
                customViewHolder.base.setBackgroundColor(R.color.green_600)
            }
        } catch(e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return feedItemList?.size ?: 0
    }

    inner class CustomViewHolder(internal var base: View) : RecyclerView.ViewHolder(base) {

        internal var snom: TextView
        internal var score: TextView
        internal var reflex: TextView
        internal var name: TextView


        init {
            snom = base.findViewById(R.id.snom) as TextView
            score = base.findViewById(R.id.score) as TextView
            reflex = base.findViewById(R.id.reflex) as TextView
            name = base.findViewById(R.id.name) as TextView


        }
    }




}

