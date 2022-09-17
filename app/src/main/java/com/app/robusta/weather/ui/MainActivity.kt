package com.app.robusta.weather.ui

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.robusta.weather.databinding.ActivityMainBinding
import com.app.robusta.weather.db.entities.HistoryWeatherItem
import com.app.robusta.weather.ui.adapter.HistoryAdapter
import com.app.robusta.weather.ui.domain.WeatherResponse
import com.app.robusta.weather.ui.domain.WeatherViewModel
import com.app.robusta.weather.uitls.handlePermissionResult
import com.app.robusta.weather.uitls.setRetrievedImageIntoView
import com.app.robusta.weather.uitls.viewShot
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<WeatherViewModel>()
    lateinit var photo:Bitmap
    lateinit var uri:Uri
    var itemsAdapter = HistoryAdapter()
    private val startCamIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            setRetrievedImageIntoView(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.showAllItemsInDb()
        if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(CAMERA), 1)
        }
        addItemToHistory()
        cancel()
        showAllHistoryData()
        openCamera()
        binding.ivShare.setOnClickListener {
            viewShot( window.decorView.findViewById(android.R.id.content))
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        binding.addNewStatus.setOnClickListener {
            startCamIntent.launch(cameraIntent)
        }
    }

    private fun showAllHistoryData() {
        viewModel.items.observe(this) {
            it?.let {
                binding.tvNotResult.isVisible = it.isEmpty()
                binding.rvList.isVisible = it.isNotEmpty()
                itemsAdapter.items = it as ArrayList<HistoryWeatherItem>
                binding.rvList.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = itemsAdapter
                }
            }
        }
        viewModel.onItemInserted.observe(this){
            it?.let {
                itemsAdapter.addItem(it)
                binding.rvList.smoothScrollToPosition(0)
            }
        }
    }

    private fun cancel() {
        binding.ivCancel.setOnClickListener {
            binding.lyAdd.isVisible = false
            binding.lyHistory.isVisible = true
        }
    }

    private fun addItemToHistory() {
        binding.ivAdd.setOnClickListener {
            binding.lyAdd.isVisible = false
            binding.lyHistory.isVisible = true
            viewModel.save(photo)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
        viewModel.data.observe(this) {
            it?.let {
                bindDataAfterTakePicture(it)
            }
        }
    }

    private fun bindDataAfterTakePicture(it: WeatherResponse) {
        binding.tvLocation.text = it.location.toString()
        binding.tvDate.text = it.current.last_updated
        binding.tvDegree.text = "${it.current.feelslike_c}C\n${it.current.feelslike_f}F\n${it.current.condition.text}"
        val url = "http://${it.current.condition.icon.replace("//", "")}"
        Glide.with(this).load(
            url
        ).centerCrop().into(binding.imageView)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this.handlePermissionResult(requestCode, permissions, grantResults)
    }

}