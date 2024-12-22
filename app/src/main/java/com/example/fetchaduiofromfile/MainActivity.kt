package com.example.fetchaduiofromfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.fetchaduiofromfile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var extractType = 0
    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFilePickerLauncher()
        binding.addAudio.setOnClickListener {
            extractType = 1
            pickAudioFile()
        }

    }

    private fun initFilePickerLauncher() {
        filePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedAudioUri: Uri? = result.data?.data
                selectedAudioUri?.let {
                    val res = getFilePathFromUri(it)
                    Log.d("MY_TAG", "Audio URI: $res")
                    if(res == null ){
                        Log.d("MY_TAG", "Path of file : is NULL")

                    }else{
                        Log.d("MY_TAG", "Path of file : $res")
                    }

                }
            }
        }
    }
    fun getFilePathFromUri(uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":")
            val type = split[0]
            val relativePath = split[1]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().absolutePath + "/" + relativePath
            }
        }
        return null
    }

    private fun pickAudioFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "audio/*" // Filter for audio files
        }
        filePickerLauncher.launch(Intent.createChooser(intent, "Select an Audio File"))
    }
}