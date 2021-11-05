package nl.com.lucianoluzzi.mapinha.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog

fun Activity.requestPermissionOr(
    permission: String,
    permissionRationaleAction: (() -> Unit)? = null,
    activityResultLauncher: ActivityResultLauncher<String>? = null,
    action: () -> Unit
) {
    val permissionCheckResult = checkSelfPermission(permission)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        action()
    } else {
        if (shouldShowRequestPermissionRationale(permission)) {
            permissionRationaleAction?.invoke() ?: run {
                activityResultLauncher?.launch(permission)
            }
        } else {
            activityResultLauncher?.launch(permission)
        }
    }
}

fun Context.showAlertDialog(
    title: String? = null,
    message: String? = null,
    positiveButtonText: String,
    negativeButtonText: String? = null,
    negativeButtonClickAction: (() -> Unit)? = null,
    positiveButtonClickAction: (() -> Unit)? = null
) {
    val builder = AlertDialog.Builder(this)
    title?.let {
        builder.setTitle(it)
    }
    builder.setPositiveButton(positiveButtonText) { dialog, _ ->
        positiveButtonClickAction?.invoke()
        dialog.dismiss()
    }
    negativeButtonClickAction?.let {
        val negativeMessage = negativeButtonText ?: "Cancel"
        builder.setNegativeButton(negativeMessage) { dialog, _ ->
            it()
            dialog.dismiss()
        }
    }
    message?.let {
        builder.setMessage(it)
    }
    builder.show()
}