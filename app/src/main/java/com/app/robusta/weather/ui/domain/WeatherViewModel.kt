package com.app.robusta.weather.ui.domain

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.robusta.weather.db.WeatherDb
import com.app.robusta.weather.db.entities.HistoryWeatherItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val usecase: WeatherUsecase,
    val dao: WeatherDb
) : ViewModel() {
    val data = MutableLiveData<WeatherResponse>()
    val items = MutableLiveData<List<HistoryWeatherItem>>()
    val onItemInserted = MutableLiveData<HistoryWeatherItem>()


    fun getData() {
        viewModelScope.launch {
            usecase.getWeatherData().collect {
                data.value = it
            }
        }
    }

    fun save(photo: Bitmap) {
        data.value?.let { item ->
            viewModelScope.launch {
                val item = HistoryWeatherItem(
                    photo = photo,
                    time = item.current.last_updated,
                    icon = item.current.condition.icon,
                    location = item.location.toString(),
                    status = "${item.current.feelslike_c}C\n${item.current.feelslike_f}F\n",
                    temperature = item.current.condition.text
                )
                dao.weatherDao().addNewWeatherStatusToHistory(
                    item
                )

                onItemInserted.value = item

            }
        }
    }

    fun showAllItemsInDb() {
        viewModelScope.launch(IO) {
            val list   = dao.weatherDao().getAllWeatherHistory()
            if (list.isNotEmpty())
                items.postValue(list)
             else items.postValue(listOf())
            this.cancel()
        }
    }
}