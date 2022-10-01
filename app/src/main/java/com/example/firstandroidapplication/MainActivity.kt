package com.example.firstandroidapplication

import android.Manifest
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.firstandroidapplication.databinding.ActivityMainBinding
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException
import java.security.AccessController.getContext
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var weatherRVModalArrayList : ArrayList<WeatherRVModal>
    lateinit var weatherRVAdapter: WeatherRVAdapter
    lateinit var locationManager : LocationManager
    var PERMISSION_CODE = 1
    lateinit var cityName :String

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        //setContentView(R.layout.activity_main)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        weatherRVModalArrayList = ArrayList()
        weatherRVAdapter = WeatherRVAdapter(this, weatherRVModalArrayList)
        binding.idIVRvWeather.adapter = weatherRVAdapter

        locationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager)
        if( checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
            checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED    ){
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION ), PERMISSION_CODE )
        }
        var location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        cityName = getCityName(location!!.longitude, location!!.latitude)

        getWeather(cityName)

        binding.idIVSearch.setOnClickListener(View.OnClickListener {
            @Override
            fun onClick(v : View){
                var city = binding.idEdtCity.text.toString()
                if(city.isEmpty()){
                    Toast.makeText(this, "No city entered", Toast.LENGTH_SHORT).show()
                } else {
                    binding.idIVCityName.text = city
                    getWeather(city)
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(PERMISSION_CODE==requestCode){
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    fun getCityName( longitude: Double, latitude : Double) : String{
        var city = "Not found"
        var gcd = Geocoder(baseContext, Locale.getDefault())
        try {
            var adresses = gcd.getFromLocation(latitude, longitude, 10)
            for(adr in adresses){
                if(adr!=null){
                    var cityName =  adr.locality
                    if(cityName!=null && cityName!=""){
                        city = cityName
                    } else {
                        Log.d("TAG", "CITY NOT FOUND")
                        Toast.makeText(this, "User city not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch(e : IOException){
            e.printStackTrace()
        }
        return city
    }
    fun getWeather(cityName : String){
        var url = "http://api.weatherapi.com/v1/forecast.json?key=a397802723824af885e175852222809&q=" + cityName + "&days=1&aqi=no&alerts=no"
        this.binding.idIVCityName.text = cityName
        var requestQueue = Volley.newRequestQueue(this@MainActivity)
        var jsonObjectRequest = JsonObjectRequest(url, null, Response.Listener<JSONObject> {
            fun onResponse (response : JSONObject){
                this.binding.idPBLoading.visibility = View.GONE
                this.binding.homeRL.visibility = View.VISIBLE
                weatherRVModalArrayList.clear()
                var temperature = response.getJSONObject("current").getString("temp_c")
                binding.idIVTemperature.text = temperature + "°C"
            }
        }, Response.ErrorListener {
            fun onErrorResponse(error : VolleyError){
                Toast.makeText(
                    this, "Please enter valid city name", Toast.LENGTH_SHORT
                ).show()
            }
        })

        requestQueue.add(jsonObjectRequest)
    }
}
