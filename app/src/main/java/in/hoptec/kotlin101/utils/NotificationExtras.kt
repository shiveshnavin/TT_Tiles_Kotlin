package `in`.hoptec.kotlin101.utils

/**
 * Created by shivesh on 23/2/17.
 */


import android.app.Notification
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.TextView

import java.lang.reflect.Method

object NotificationExtras {

    // Method reference to Notification.Builder#makeContentView
    private val MAKE_CONTENT_VIEW_METHOD: Method?

    init {
        var m: Method? = null
        try {
            m = Notification.Builder::class.java.getDeclaredMethod("makeContentView")
            m!!.isAccessible = true
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

        MAKE_CONTENT_VIEW_METHOD = m
    }

    /* Public */

    fun buildWithBackgroundResource(context: Context, builder: Notification.Builder, @DrawableRes res: Int): Notification {
        if (MAKE_CONTENT_VIEW_METHOD == null) return buildNotification(builder)
        val remoteViews = obtainRemoteViews(builder)
        val notification = buildNotification(builder)

        // Find the root of the content view and apply the background to it
        if (remoteViews != null) {
            val v = LayoutInflater.from(context).inflate(remoteViews.layoutId, null)
            remoteViews.setInt(v.id, "setBackgroundResource", res)
        }

        return notification
    }

    fun buildWithBackgroundColor(context: Context, builder: Notification.Builder, @ColorInt color: Int): Notification {
        if (MAKE_CONTENT_VIEW_METHOD == null) return buildNotification(builder)
        val remoteViews = obtainRemoteViews(builder)
        val notification = buildNotification(builder)

        // Find the root of the content view and apply the color to it
        if (remoteViews != null) {
            val v = LayoutInflater.from(context).inflate(remoteViews.layoutId, null)
            remoteViews.setInt(v.id, "setBackgroundColor", color)

            // Calculate a contrasting text color to ensure readability, and apply it to all TextViews within the notification layout
            val useDarkText = Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114 > 186
            val textColor = if (useDarkText) 0xff000000.toInt() else 0xffffffff.toInt()
            applyTextColorToRemoteViews(remoteViews, v, textColor)
        }

        return notification
    }

    /* Private */

    private fun obtainRemoteViews(builder: Notification.Builder): RemoteViews? {
        try {
            // Explicitly force creation of the content view and re-assign it to the notification
            val remoteViews = MAKE_CONTENT_VIEW_METHOD!!.invoke(builder) as RemoteViews
            builder.setContent(remoteViews)
            return remoteViews

        } catch (ignored: Throwable) {
            return null
        }

    }

    private fun buildNotification(builder: Notification.Builder): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build()
        } else {
            return builder.notification
        }
    }

    private fun applyTextColorToRemoteViews(remoteViews: RemoteViews, view: View, color: Int) {
        if (view is ViewGroup) {
            val vg = view
            var i = 0
            val count = vg.childCount
            while (i < count) {
                applyTextColorToRemoteViews(remoteViews, vg.getChildAt(i), color)
                i++
            }
        } else if (view is TextView) {
            remoteViews.setTextColor(view.getId(), color)
        }
    }
}// no instance
