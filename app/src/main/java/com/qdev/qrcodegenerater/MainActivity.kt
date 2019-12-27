package com.qdev.qrcodegenerater

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.math.BigInteger
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    internal var bitmap: Bitmap? = null
    private var edtname: EditText? = null
    private var edtage: EditText? = null
    private var edtplace: EditText? = null
    private var showQr: ImageView? = null
    private var generateQr: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions,0)
        showQr =this.image
        edtage = this.edt_age
        edtname = this.edt_name
        edtplace = this.edt_palce
        generateQr = this.generate

        generateQr?.setOnClickListener {
            if (edtname?.text.toString().length == 0) {
                Toast.makeText(this@MainActivity, "Enter Name!", Toast.LENGTH_SHORT).show()
            }else if (edtplace?.text.toString().length == 0) {
            Toast.makeText(this@MainActivity, "Enter Place!", Toast.LENGTH_SHORT).show()
        }else if (edtage?.text.toString().length == 0) {
            Toast.makeText(this@MainActivity, "Enter Age!", Toast.LENGTH_SHORT).show()
        } else {
                try {
                    var value = edtname?.text.toString()+"\n"+
                            edtplace?.text.toString()+"\n"+
                            edtage?.text.toString()
                            bitmap = generateQrCode(value)
                    showQr?.setImageBitmap(bitmap)
             //       val path = saveImage(bitmap)  //give read write permission
                 //   Toast.makeText(this@MainActivity, "QRCode saved to -> $path", Toast.LENGTH_SHORT).show()
                } catch (e: WriterException) {
                    e.printStackTrace()
                }

            }
        }
    }

    /*fun saveImage(myBitmap: Bitmap?): String {
        val bytes = ByteArrayOutputStream()
        myBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)


        if (!wallpaperDirectory.exists()) {
            Log.d("QR", "" + wallpaperDirectory.mkdirs())
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                .timeInMillis.toString() + ".jpg")
            f.createNewFile()   //give read write permission
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""

    }*/

    @Throws(WriterException::class)
    private fun generateQrCode(Value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {

            //val md5 = Value.md5()
           // println("computed md5 value is $md5")
            bitMatrix = MultiFormatWriter().encode(
                Value,
                BarcodeFormat.QR_CODE,
                QRWidth, QRWidth, null
            )

        } catch (Illegalargumentexception: IllegalArgumentException) {

            return null
        }

        val bitWidth = bitMatrix.getWidth()

        val bitHeight = bitMatrix.getHeight()

        val pixels = IntArray(bitWidth * bitHeight)

        for (y in 0 until bitHeight) {
            val offset = y * bitWidth

            for (x in 0 until bitWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    resources.getColor(R.color.black)
                else
                    resources.getColor(R.color.white)
            }
        }
        val bitmap = Bitmap.createBitmap(bitWidth, bitHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 700, 0, 0, bitWidth, bitHeight)
        return bitmap
    }

    companion object {

        val QRWidth = 700
      //  private val IMAGE_DIRECTORY = "/QRcode"
    }
   /* fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }*/
}