package `in`.hoptec.kotlin101

import com.google.gson.Gson

/**
 * Created by shivesh on 14/12/17.
 */

class Score {

    var dateTime: String? = null
    var name: String? = "Anonymous"
    var fireId: String? = null
    var score: Int? = null
    var reflex: Float? = null

    fun toJson() : String
    {
        var js=Gson()
        return js.toJson(this)
    }


}
