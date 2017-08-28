package `in`.hoptec.kotlin101.utils

/**
 * Created by shivesh on 14/7/17.
 */

interface GenricCallback {


    fun onStart()
    fun onGameEnd(score: Any)
    fun onDo(obj: Any, obj2: Any)
    fun onUpdate(score: Any)


}
