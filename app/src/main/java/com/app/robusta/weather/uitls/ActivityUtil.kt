package com.app.robusta.weather.uitls

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.app.robusta.weather.ui.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun MainActivity.handlePermissionResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
){
    if (requestCode == 1) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
           // startCamIntent.launch(cameraIntent,)
        } else {
            Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
        }
    }
}

fun MainActivity.setRetrievedImageIntoView(result: ActivityResult) {
    if (result.resultCode == Activity.RESULT_OK) {
        val i = result.data
         result.data?.data?.let {
             uri = it
         }
        photo = i?.extras!!["data"] as Bitmap
        binding.ivPic.setImageBitmap(photo)
        binding.lyHistory.isVisible = false
        binding.lyAdd.isVisible = true
        binding.tvNotResult.isVisible = false
    }
}

 fun MainActivity.saveImageExternal(image: Bitmap): Uri? {
    var uri: Uri? = null
    try {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png")
        val stream = FileOutputStream(file)
        image.compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.close()
        uri = Uri.fromFile(file)
    } catch (e: IOException) {
    }
    return uri
}

fun MainActivity.viewShot(v: View){
    val height: Int = v.height
    val width: Int = v.width
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    v.layout(0, 0, v.layoutParams.width, v.layoutParams.height)
    v.draw(c)
    store( b,"Weather Now")
}

fun MainActivity.store(bm: Bitmap, fileName: String?) {
    val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/Screenshots"
    val dir = File(dirPath)
    if (!dir.exists()) dir.mkdirs()
    val file = File(dirPath, fileName)
    try {
        val fOut = FileOutputStream(file)
        bm.compress(Bitmap.CompressFormat.PNG, 85, fOut)
        fOut.flush()
        fOut.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    shareImage(file)
}

fun MainActivity.shareImage(file: File) {
    val uri = FileProvider.getUriForFile(this, this.applicationContext.packageName + ".provider", file);
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_SUBJECT, "")
    intent.putExtra(Intent.EXTRA_TEXT, "")
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    try {
        startActivity(Intent.createChooser(intent, "Share Screenshot"))
    } catch (e: ActivityNotFoundException) {
    }
}