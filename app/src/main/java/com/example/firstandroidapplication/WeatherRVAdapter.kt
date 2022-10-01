package com.example.firstandroidapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firstandroidapplication.databinding.ActivityMainBinding
import com.example.firstandroidapplication.databinding.WeatherRvItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date


class WeatherRVAdapter() : RecyclerView.Adapter<WeatherRVAdapter.ViewHolder>() {
    lateinit var context : Context
    lateinit var weatherRVModalArrayList : ArrayList<WeatherRVModal>

    constructor(context: Context, weatherRVModalArrayList: ArrayList<WeatherRVModal>) : this() {
        this.context = context
        this.weatherRVModalArrayList = weatherRVModalArrayList
    }

    open class ViewHolder(private val itemView : View) : RecyclerView.ViewHolder(itemView ) {
        private lateinit var binding: WeatherRvItemBinding
        //binding= DataBindingUtil.setContentView( WeatherRvItemBinding, itemView)
        var windTV : TextView;
        var timeTV : TextView;
        var temperatureTV : TextView;
        var conditionIV : ImageView;

        init{
            this.windTV = binding.idTVWindSpeed
            this.timeTV = binding.idTVTime
            this.temperatureTV = binding.idTVTemperature
            this.conditionIV = binding.idIVCondition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ViewHolder {
        var view : View = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modal = weatherRVModalArrayList.get(position)
        //get the image of the weather from the element and display it
        Picasso.get().load("http://".plus(modal.icon)).into(holder.conditionIV)
        holder.temperatureTV.setText(modal.temperature + "°C")
        holder.windTV.setText(modal.windSpeed + "km/h")
        var input  = SimpleDateFormat("yyyy-MM-dd hh:mm")
        var output = SimpleDateFormat("hh:mm aa")
        var t :Date = input.parse(modal.time)
        holder.timeTV.setText( output.format(t) )

    }

    override fun getItemCount(): Int {
        return weatherRVModalArrayList.size
    }


}