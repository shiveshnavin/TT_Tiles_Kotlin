package `in`.hoptec.kotlin101

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.provider.MediaStore
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AlertDialog
import android.support.v7.view.ContextThemeWrapper
import android.text.Html
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.UUID


import `in`.hoptec.kotlin101.utils.FileOperations
import `in`.hoptec.kotlin101.utils.GenricCallback

import android.content.Context.MODE_PRIVATE

/**
 * Created by shivesh on 28/6/17.
 */

class utl {

    private fun isValidMail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess.toLowerCase().contains(context.packageName) || context.packageName.toLowerCase().contains(activeProcess.toLowerCase())) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo.packageName.toLowerCase().contains(context.packageName) || context.packageName.toLowerCase().contains(componentInfo.packageName.toLowerCase())) {
                isInBackground = false
            }
        }

        return isInBackground
    }


    interface ClickCallBack {

        fun done(dialogInterface: DialogInterface)

    }


    interface InputDialogCallback {
        fun onDone(text: String)
    }

    companion object {


        var DISPLAY_ENABLED = true
        var DEBUG_ENABLED = true


        var ctx : Context?=null

        /** */
        fun init( cx:Context?)
        {

            ctx=cx

        }




        fun animate(app: View, property: String, initv: Int, finalv: Int, repeat: Boolean, dur: Int) {


            val colorAnim = ObjectAnimator.ofInt(app, property,
                    initv, finalv)
            colorAnim.setEvaluator(ArgbEvaluator())

            if (repeat) {
                colorAnim.repeatMode = ValueAnimator.REVERSE
                colorAnim.repeatCount = ValueAnimator.INFINITE
            }

            colorAnim.duration = dur.toLong()
            colorAnim.start()

        }


        fun animateBackGround(app: View, initcolor: String, finalcolor: String, repeat: Boolean, dur: Int) {

            val property = "backgroundColor"
            val colorAnim = ObjectAnimator.ofInt(app, property,
                    Color.parseColor(initcolor), Color.parseColor(finalcolor))
            colorAnim.setEvaluator(ArgbEvaluator())

            if (repeat) {
                colorAnim.repeatMode = ValueAnimator.REVERSE
                colorAnim.repeatCount = ValueAnimator.INFINITE
            }

            colorAnim.duration = dur.toLong()
            colorAnim.start()

        }



        fun fullScreen(act: Activity) {
            try {
                act.requestWindowFeature(Window.FEATURE_NO_TITLE)
                act.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        fun getH(ctx: Context): Int {
            val windowManager = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            return windowManager.defaultDisplay.height

        }


        fun getW(ctx: Context): Int {
            val windowManager = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            return windowManager.defaultDisplay.width

        }


        fun SlideUP(view: View, context: Context) {
            view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.slid_up))
        }

        fun SlideDown(view: View, context: Context) {
            view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.slid_down))
        }


        var CLAN_PRO_NORMAL = "ClanPro-Book.otf"
        var AVENIR_MED = "Avenir-Medium.ttf"
        var CAVIAR = "CaviarDreams.ttf"
        var ROBOTO_THIN = "Roboto-Thin.ttf"


        fun getFace(font: String, ctx: Context): Typeface? {
            var face: Typeface? = Typeface.createFromAsset(ctx.assets,
                    "fonts/" + font)

            if (face == null) {
                face = Typeface.createFromAsset(ctx.assets,
                        "fonts/" + AVENIR_MED)
            }
            return face

        }


        fun setFace(font: String, textView: TextView): Typeface? {
            val ctx = textView.context
            var face: Typeface? = Typeface.createFromAsset(ctx.assets,
                    "fonts/" + font)

            if (face == null) {
                face = Typeface.createFromAsset(ctx.assets,
                        "fonts/" + AVENIR_MED)
            }
            textView.typeface = face
            return face

        }


        fun changeTextColor(textView: TextView?, startColor: Int, endColor: Int,
                            animDuration: Long, animUnit: Long) {
            if (textView == null) return

            val startRed = Color.red(startColor)
            val startBlue = Color.blue(startColor)
            val startGreen = Color.green(startColor)

            val endRed = Color.red(endColor)
            val endBlue = Color.blue(endColor)
            val endGreen = Color.green(endColor)

            val ct = object : CountDownTimer(animDuration, animUnit) {
                //animDuration is the time in ms over which to run the animation
                //animUnit is the time unit in ms, update color after each animUnit

                override fun onTick(l: Long) {
                    val red = (endRed + l * (startRed - endRed) / animDuration).toInt()
                    val blue = (endBlue + l * (startBlue - endBlue) / animDuration).toInt()
                    val green = (endGreen + l * (startGreen - endGreen) / animDuration).toInt()

                    textView.setTextColor(Color.rgb(red, green, blue))
                }

                override fun onFinish() {
                    textView.setTextColor(Color.rgb(endRed, endGreen, endBlue))

                }
            }

            ct.start()
        }


        fun refineString(red: String, rep: String): String {
            var red = red
            red = red.replace("[^a-zA-Z0-9]".toRegex(), rep)
            return red
        }


        fun isValidMobile(phone: String): Boolean {
            return android.util.Patterns.PHONE.matcher(phone).matches()
        }

        val time: String
            get() {
                val dateFormat = SimpleDateFormat("HH:mm:ss")
                val date = Date()
                return dateFormat.format(date)
            }

        val date: String
            get() {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                val date = Date()
                return dateFormat.format(date)
            }

        val dateTime: String
            get() {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val date = Date()
                return dateFormat.format(date)
            }


        var TAG = "TAH UTL"
        fun log(t: String) {

            Log.d("" + TAG, "" + t)
        }


        fun log(t: String, tt: String) {

            Log.d("" + t, "" + tt)
        }


        fun l(t: String) {

            Log.d("" + TAG, "" + t)
        }


        fun l(t: String, tt: String) {

            Log.d("" + t, "" + tt)
        }


        fun e(t: String) {

            Log.e("" + TAG, "" + t)
        }

        fun e(t: String, tt: String) {

            Log.e("" + t, "" + tt)
        }

        fun l(t: Any) {

            Log.d("" + TAG, "" + t)
        }


        fun l(t: String, tt: Any) {

            Log.d("" + t, "" + tt)
        }


        fun getApkVerison(ctx: Context): Int {
            try {
                val versionCode = BuildConfig.VERSION_CODE
                val versionName = BuildConfig.VERSION_NAME
                return versionCode
            } catch (e: Exception) {
                e.printStackTrace()
                return 0

            }

        }


        fun changeColorDrawable(imageView: ImageView, @ColorRes res: Int) {

            try {
                DrawableCompat.setTint(imageView.drawable, ContextCompat.getColor(ctx, res))
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }


        fun getDominantColor1(bitmap: Bitmap?): Int {

            if (bitmap == null)
                throw NullPointerException()

            val width = bitmap.width
            val height = bitmap.height
            val size = width * height
            val pixels = IntArray(size)

            val bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false)

            bitmap2.getPixels(pixels, 0, width, 0, 0, width, height)

            val colorMap = ArrayList<HashMap<Int, Int>>()
            colorMap.add(HashMap<Int, Int>())
            colorMap.add(HashMap<Int, Int>())
            colorMap.add(HashMap<Int, Int>())

            var color = 0
            var r = 0
            var g = 0
            var b = 0
            var rC: Int?
            var gC: Int?
            var bC: Int?
            for (i in pixels.indices) {
                color = pixels[i]

                r = Color.red(color)
                g = Color.green(color)
                b = Color.blue(color)

                rC = colorMap[0][r]
                if (rC == null)
                    rC = 0
                colorMap[0].put(r, ++rC)

                gC = colorMap[1][g]
                if (gC == null)
                    gC = 0
                colorMap[1].put(g, ++gC)

                bC = colorMap[2][b]
                if (bC == null)
                    bC = 0
                colorMap[2].put(b, ++bC)
            }

            val rgb = IntArray(3)
            for (i in 0..2) {
                var max = 0
                var `val` = 0
                for ((key, value) in colorMap[i]) {
                    if (value > max) {
                        max = value
                        `val` = key
                    }
                }
                rgb[i] = `val`
            }

            val dominantColor = Color.rgb(rgb[0], rgb[1], rgb[2])

            return dominantColor
        }


        fun dpFromPx(context: Context, px: Float): Float {
            return px / context.resources.displayMetrics.density
        }

        fun pxFromDp(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }


        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }


        fun showSoftKeyboard(activity: Activity, linearLayout: View) {

            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(
                    linearLayout.applicationWindowToken,
                    InputMethodManager.SHOW_FORCED, 0)
        }


        fun snack(act: Activity, t: String) {

            val rootView = act.window.decorView.rootView
            val snackbar = Snackbar.make(rootView, "" + t, Snackbar.LENGTH_LONG)
            // snackbar.setActionTextColor(act.getResources().getColor(R.color.material_red_A400));
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(act.resources.getColor(R.color.red_300))

            val snackbarTextId = android.support.design.R.id.snackbar_text
            val textView = snackbarView.findViewById(snackbarTextId) as TextView
            textView.setTextColor(Color.WHITE)

            if (DISPLAY_ENABLED)
                snackbar.show()

        }


        fun snack(rootView: View, t: String) {

            val act = rootView.context

            val snackbar = Snackbar.make(rootView, "" + t, Snackbar.LENGTH_LONG)
            // snackbar.setActionTextColor(act.getResources().getColor(R.color.material_red_A400));
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(act.resources.getColor(R.color.red_300))

            val snackbarTextId = android.support.design.R.id.snackbar_text
            val textView = snackbarView.findViewById(snackbarTextId) as TextView
            textView.setTextColor(Color.WHITE)

            if (DISPLAY_ENABLED)
                snackbar.show()

        }


        fun snack(rootView: View, t: String, a: String, cb: GenricCallback) {

            val act = rootView.context

            val snackbar = Snackbar.make(rootView, "" + t, Snackbar.LENGTH_LONG)
            snackbar.setActionTextColor(act.resources.getColor(R.color.grey_100))
            snackbar.setAction("" + a) { cb.onStart() }
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(act.resources.getColor(R.color.red_300))

            val snackbarTextId = android.support.design.R.id.snackbar_text
            val textView = snackbarView.findViewById(snackbarTextId) as TextView
            textView.setTextColor(Color.WHITE)

            if (DISPLAY_ENABLED)
                snackbar.show()

        }

        fun diag(c: Context, title: String, desc: String, action: String, click: ClickCallBack) {


            val alertDialogBuilder = AlertDialog.Builder(c)
            alertDialogBuilder.setTitle(title)
            alertDialogBuilder.setMessage(desc)
            alertDialogBuilder.setNeutralButton(action) { dialogInterface, i -> click.done(dialogInterface) }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


        fun diag(c: Context, title: String, desc: String, isCancellable: Boolean, action: String, click: ClickCallBack) {


            val alertDialogBuilder = AlertDialog.Builder(c)
            alertDialogBuilder.setTitle(title)
            alertDialogBuilder.setMessage(desc)
            alertDialogBuilder.setCancelable(isCancellable)
            alertDialogBuilder.setNeutralButton(action) { dialogInterface, i -> click.done(dialogInterface) }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


        fun diag(c: Context, title: String, desc: String) {
            try {
                val alertDialogBuilder = AlertDialog.Builder(c)
                alertDialogBuilder.setTitle(title)
                alertDialogBuilder.setMessage(Html.fromHtml(desc))
                alertDialogBuilder.setNeutralButton("OK") { dialog, id -> dialog.cancel() }


                val alertDialog = alertDialogBuilder.create()


                alertDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        fun toast(c: Context, t: String) {


            try {
                if (DISPLAY_ENABLED)
                    Toast.makeText(c, t, Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        var TYPE_EMAIL: Int? = 120
        var TYPE_PHONE: Int? = 293
        var TYPE_DEF: Int? = 101


        var input: EditText? = null



        val MY_PREFS_NAME = "wootwoot"
        var editor: SharedPreferences.Editor? = null// = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();


        fun copyFile(src: File, dst: File) {
            try {

                if (!src.exists())
                    return
                val `in` = FileInputStream(src)
                val os = FileOutputStream(dst)

                val buf = ByteArray(1024)
                var len: Int = `in`.read(buf)
                while ((len) > 0) {

                    os.write(buf, 0, len)
                    len= `in`.read(buf)
                }
                `in`.close()
                os.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }


        fun deleteDir(dir: File): Boolean {
            if (dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }

            // The directory is now empty so delete it
            return dir.delete()
        }


        fun uid(l: Int): String {
            val uuid = UUID.randomUUID().toString().replace("-".toRegex(), "")
            println("uuid Full= " + uuid)
            val ret = uuid.substring(0, Math.min(uuid.length, l))
            println("uuid $l = $ret")
            return ret
        }


        fun saveVideoThumb(filenameTarget: String, video: String): Boolean {
            val bmp: Bitmap

            bmp = ThumbnailUtils.createVideoThumbnail(video, MediaStore.Video.Thumbnails.MINI_KIND)

            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(filenameTarget)
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    if (out != null) {
                        out.close()
                        return true
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                }

                return false
            }


        }
    }


}