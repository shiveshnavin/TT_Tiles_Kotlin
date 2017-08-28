package `in`.hoptec.kotlin101

import android.app.ActivityOptions.makeSceneTransitionAnimation
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utl.fullScreen(this)
        setContentView(R.layout.activity_splash)
        val iit = Intent(this, MainActivity::class.java)


        activity_splash.setOnClickListener {
            startActivity(iit)
        }


        var H:Handler= Handler()
        H.postDelayed(Runnable {
/*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, name, getString(R.string.tran))
            startActivity(it, options.toBundle())
        } else {
            startActivity(it)
        }*/

            startActivity(iit)
        },2000)






    }
}
