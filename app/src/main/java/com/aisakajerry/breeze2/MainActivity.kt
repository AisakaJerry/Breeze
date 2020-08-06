package com.aisakajerry.breeze2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import okhttp3.*
import okhttp3.HttpUrl
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().build()
    private val gistJsonAdapter = moshi.adapter(Gist::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.searchButton)
        button.setOnClickListener {
            runSearch()
        }
        //runSearch()
    }
    fun runSearch() {
        println("Start to fetch URL json")

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("www.google.com")
            .addPathSegment("search")
            .addQueryParameter("q", "fly me to the moon")
            .build()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val gist = gistJsonAdapter.fromJson(response.body!!.source())

                for ((key, value) in gist!!.files!!) {
                    println(key)
                    println(value.content)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Fail to execute request")
            }
        })
    }
    @JsonClass(generateAdapter = true)
    data class Gist(var files: Map<String, GistFile>?)

    @JsonClass(generateAdapter = true)
    data class GistFile(var content: String?)
}