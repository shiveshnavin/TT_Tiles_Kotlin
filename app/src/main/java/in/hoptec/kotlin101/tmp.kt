package `in`.hoptec.kotlin101

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener

/**
 * Created by shivesh on 14/11/17.
 */

class tmp {
    init {
        AndroidNetworking.get("").build().getAsString(object : StringRequestListener {
            override fun onResponse(response: String) {

            }

            override fun onError(ANError: ANError) {

            }
        })
    }
}
