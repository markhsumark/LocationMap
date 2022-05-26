package com.mark.locationmap

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity(), LocationListener {

    lateinit var tv_loc: TextView
    lateinit var btn_GMAP: Button
    lateinit var btn_dis: Button
    var loc: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_loc = findViewById(R.id.tvloc)
        btn_GMAP = findViewById(R.id.btn_map)
        btn_GMAP.setOnClickListener{intentGMAP()}
        btn_dis = findViewById(R.id.btn_dis)
        btn_dis.setOnClickListener{showDistance2NTOU()}


        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            1)
        }
        locationInit()

    }

    private fun intentGMAP() {
        locationInit()
//        Emulator的目前位置設在新竹市
        val temp = loc
        if(temp != null) {
            var strUrl =
                "http://maps.google.com/maps?f=d&saddr=" + temp.latitude.toString() + "," + temp.longitude.toString() + "&daddr=基隆市北寧路2號"
            var intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(strUrl)
            startActivity(intent)
        }
    }

    private fun showDistance2NTOU(){
//        25.147706°N 121.771736°E
        locationInit()
        val start = loc
        val endLongi = 121.771736
        val endLati = 25.147706
        if(start != null) {
            var dis :FloatArray = FloatArray(2)
            Location.distanceBetween(start.latitude, start.longitude, endLati, endLongi, dis)
            var resultText = String.format("距離%.2f公里", dis[0]/1000)
            Toast.makeText(this, resultText, Toast.LENGTH_SHORT).show()

//            使用distanceTo()的方法

//            var target = Location(LocationManager.GPS_PROVIDER)
//            target.setLatitude(endLati)
//            target.setLongitude(endLongi)
//            val result = start.distanceTo(target)
//            resultText = String.format("距離%.2f公里", result/1000)
//            Toast.makeText(this, resultText, Toast.LENGTH_SHORT).show()

        }

        
    }


    private fun locationInit() {
        var locmg = getSystemService(LOCATION_SERVICE) as LocationManager
        try{
            loc = locmg.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }catch(e: SecurityException) {
            
        }
        try {
            locmg.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000, 1f, this)
        } catch (e: SecurityException) {
        }
        showLocation()
    }

    private fun showLocation(){
        val temp = loc
        if(temp != null) {
            var msg= StringBuffer()
            msg.append("目前位置")
            msg.append(temp.longitude.toString())
            msg.append(",")
            msg.append(temp.latitude.toString())
            tv_loc.text = msg.toString()
        }else{
            Toast.makeText(this, "無法取得現在位置", Toast.LENGTH_SHORT).show()
            tv_loc.text = "Cannot get location!"
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode,
            permissions, grantResults)

        if ((grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED)) {
            locationInit()
        }
    }
    override fun onLocationChanged(p0: Location) {

    }
}