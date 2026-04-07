package com.digitral.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.digitral.filepicker.callback.FilePickerCallback

import com.digitral.myapplication.databinding.ActivityMainBinding

class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    // Do not redeclare mFilePicker — it shadows BaseActivity's field; onActivityResult / pickMediaLauncher
    // read the Java field and would stay null while clicks used the Kotlin property only.
    private val callback =
        object : FilePickerCallback {
            override fun onResult(
                requestId: Int,
                status: Int,
                response: Intent?,
                returnObject: Any?,
            ) {
                Toast.makeText(this@MainActivity,"RequestId:$requestId", Toast.LENGTH_LONG).show()
                if (response == null) {
                    Toast.makeText(this@MainActivity, "Sorry you have not selected any file", Toast.LENGTH_LONG).show()
                    return
                }
                val filePath = response.getStringExtra("filePath")
                if (filePath.isNullOrEmpty()) {
                    Toast.makeText(this@MainActivity, "No file path in result", Toast.LENGTH_LONG).show()
                    return
                }
                val bitmap = BitmapFactory.decodeFile(filePath)
                if (bitmap != null) {
                    binding.ivPreview.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this@MainActivity, "Could not load image", Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mFilePicker = FilePickerImpl.newInstance(this, callback, this)
        //mFilePicker?.cropEnabled(true)?.compressEnabled(false)?.waterMarkEnabled(false)
        //initFilePicker(callback)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.camera.setOnClickListener {
            //launchCameraNew(FilePicker.REQ_CODE_CAMERA, null, callback, true, false)
            //initFilePicker(callback)
            mFilePicker?.pickImage(callback)
            //mFilePicker?.clear()
            //mFilePicker?.setCallback(callback)
            //mFilePicker?.launchCamera(1,null)
        }

        binding.gallery.setOnClickListener {
            //launchGalleryNew(FilePicker.REQ_CODE_GALLERY, null, callback, true, false)
            //initFilePicker(callback)
            mFilePicker?.pickImage(callback)
            //mFilePicker?.clear()
            //mFilePicker?.setCallback(callback)
            //mFilePicker?.launchGallery(2, null)
        }

    }

    fun launchGalleryNew(
        requestCode: Int,
        returnObject: Any?,
        callback: FilePickerCallback?,
        cropEnabled: Boolean,
        compressEnabled: Boolean,
    ) {
        System.gc()
        //initFilePicker(callback)
        mFilePicker?.compressEnabled(compressEnabled)
        if (compressEnabled) {
            mFilePicker?.compressSize(80)
        }
        mFilePicker?.cropEnabled(cropEnabled)
        mFilePicker?.launchGallery(requestCode, returnObject)
    }

    fun launchCameraNew(
        requestCode: Int,
        returnObject: Any?,
        callback: FilePickerCallback?,
        cropEnabled: Boolean,
        compressEnabled: Boolean,
    ) {
        System.gc()
        //initFilePicker(callback)
        mFilePicker?.compressEnabled(compressEnabled)
        if (compressEnabled) {
            mFilePicker?.compressSize(80)
        }
        mFilePicker?.cropEnabled(cropEnabled)
        mFilePicker?.launchCamera(requestCode, returnObject)
    }


}
