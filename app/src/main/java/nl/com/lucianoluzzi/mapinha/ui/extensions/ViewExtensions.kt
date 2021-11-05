package nl.com.lucianoluzzi.mapinha.ui.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

fun View.animateShow() {
    if (isVisible) return
    animate()
        .translationY(0f)
        .setDuration(250)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                isVisible = true
            }
        })
}

fun View.animateHide() {
    if (!isVisible) return
    val navigationBarSize = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
    } else {
        rootWindowInsets.systemWindowInsetBottom
    }
    val viewTotalHeight = height + paddingTop + paddingBottom + navigationBarSize
    animate()
        .translationY(viewTotalHeight.toFloat())
        .setDuration(250)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                isVisible = false
            }
        })
}
