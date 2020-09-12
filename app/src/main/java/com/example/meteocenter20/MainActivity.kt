package com.example.meteocenter20

import android.os.AsyncTask
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tempNum: TextView = findViewById<TextView>(R.id.tempNum)

        fun funDirection (gradus: Int): String? {
            return when {
                (gradus in 1..89) -> return "Северо-Восток"
                (gradus == 90) -> return "Восток"
                (gradus in 91..179) -> return "Юго-Восток"
                (gradus == 180) -> return "Юг"
                (gradus in 181..269) -> return "Юго-Запад"
                (gradus == 270) -> return "Запад"
                (gradus in 271..359) -> return "Северо-Запад"
                (gradus == 360 || gradus == 0) -> return "Север"
                else -> "Нет такого направления"
            }
        }

        class SomeTask : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg urls: String?): String? {
                var res: String? = null
                val url: String? = urls[0]
                try {
                    val resultGet = khttp.get(url.toString())
                    res = resultGet.text
                } catch (e: IOException) {
                    System.out.println("ERROR(IOException) $e")
                }
                return res
            }

            override fun onPostExecute(res: String?) {
                var jsonTemp: Double? = null
                var jsonCondition: String? = null
                var jsonFeelsLike: Double? = null
                var jsonDegWind: Int? = null
                var jsonWindSpeed: Int? = null
                try {
                    val jsonObj: JSONObject = JSONObject(res)
                    jsonTemp = jsonObj.getJSONObject("main").getDouble("temp")
                    jsonCondition = jsonObj.getJSONArray("weather").getJSONObject(0).getString("description")
                    jsonFeelsLike = jsonObj.getJSONObject("main").getDouble("feels_like")
                    jsonDegWind = jsonObj.getJSONObject("wind").getInt("deg")
                    jsonWindSpeed = jsonObj.getJSONObject("wind").getInt("speed")
                } catch (e: JSONException) {
                    System.out.println(e)
                }

                val resultTempString: String = "${jsonTemp?.toInt()}°"
                val resultFeelsLike: String = "${jsonFeelsLike?.toInt()}°"
                val resusltWildSpeed: String = "${jsonWindSpeed} м/с"
                val resultSpeedDirection: String = funDirection(jsonDegWind!!).toString()

                feels.text = "Ощущается: $resultFeelsLike"
                weatherCondition.text = jsonCondition!!.capitalize()
                tempNum.text = resultTempString
                windDirection.text = resultSpeedDirection
                windSpeed.text = resusltWildSpeed
            }
        }
        SomeTask().execute("http://api.openweathermap.org/data/2.5/weather?q=Ulyanovsk,RU&units=metric&lang=ru&type=like&appid=6c067958a3871f00070213b4771b7349")

        val arrayCity: Array<String> = arrayOf("Ulyanovsk", "Moscow", "Kazan")
        val arrayApi: Array<String> = arrayOf("6c067958a3871f00070213b4771b7349", "300ac852abbc70e10b5228c509ee325e", "c55aa0aac1c0a1d74e1ac7dc98656378")
        var generateUrl: String
        var indexCity: Int = 0

        fun next() {
            indexCity++
            if (indexCity >= arrayCity.size) {
                indexCity = 0
            }
            val anim: Animation = AnimationUtils.loadAnimation(this,R.anim.anim_head)
            text.startAnimation(anim)
            text.text = arrayCity[indexCity]
            generateUrl = "http://api.openweathermap.org/data/2.5/weather?q=${arrayCity[indexCity]},RU&units=metric&lang=ru&type=like&appid=${arrayApi[indexCity]}"
            SomeTask().execute(generateUrl)
        }

        fun back() {
            indexCity--
            if (indexCity < 0) {
                indexCity = arrayCity.size - 1
            }
            val anim: Animation = AnimationUtils.loadAnimation(this,R.anim.anim_head)
            text.startAnimation(anim)
            text.text = arrayCity[indexCity]
            generateUrl = "http://api.openweathermap.org/data/2.5/weather?q=${arrayCity[indexCity]},RU&units=metric&lang=ru&type=like&appid=${arrayApi[indexCity]}"
            SomeTask().execute(generateUrl)
        }

        array_rigth.setOnClickListener { next() }
        array_left.setOnClickListener { back() }
    }
}






























    

