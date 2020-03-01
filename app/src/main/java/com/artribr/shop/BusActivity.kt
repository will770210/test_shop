package com.artribr.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.row_bus.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class BusActivity : AppCompatActivity(), AnkoLogger {
    val url = "http://data.tycg.gov.tw/opendata/datalist/datasetMeta/";
    var bus:Bus? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        doAsync {
            val busService = retrofit.create(BusService::class.java)
            bus = busService.getBusList().execute().body()
            bus?.datas?.forEach{
                info("${it.BusID} ${it.RouteID} ${it.Speed}")
            }

            uiThread {
                recycler.layoutManager = LinearLayoutManager(this@BusActivity)
                recycler.setHasFixedSize(true)
                recycler.adapter = BusAdapter()
            }
        }
        
    }

    inner class BusAdapter(): RecyclerView.Adapter<BusHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_bus, parent, false)
            return BusHolder(view)
        }

        override fun getItemCount(): Int {
            return bus?.datas?.size?:0
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            val data = bus?.datas?.get(position)

            holder.bindData(data!!)
        }


    }

    inner class BusHolder(view : View): RecyclerView.ViewHolder(view){
        val busIdText: TextView = view.bus_busid
        val ruteIdText: TextView = view.bus_routeId
        val speedText: TextView = view.bus_speed

        fun bindData(data: Data){
            busIdText.text = data.BusID
            ruteIdText.text = data.RouteID
            speedText.text = data.Speed
        }
    }
}




/*
{
    "datas" : [ {
    "BusID" : "KKA-3785",
    "ProviderID" : "1",
    "DutyStatus" : "0",
    "BusStatus" : "0",
    "RouteID" : "50531",
    "GoBack" : "2",
    "Longitude" : "121.210009",
    "Latitude" : "24.866930",
    "Speed" : "0",
    "Azimuth" : "14",
    "DataTime" : "2020-03-01 10:32:45",
    "ledstate" : "0",
    "sections" : "1"
}]
}*/

data class Bus(
    val datas: List<Data>
)

data class Data(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)
interface BusService{
    @GET("download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed")
    fun getBusList(): Call<Bus>
}
