package com.example.duskplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val view = findViewById<RecyclerView>(R.id.musicRecyclerMain)
        view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        view.adapter = TestAdapter()


        val view1 = findViewById<RecyclerView>(R.id.playlistRecyclerMain)
        view1.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        view1.adapter = TestPlaylistAdapter()



        val button: FloatingActionButton = findViewById(R.id.floatTest)
        button.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.mainFragment, TestFtagment())
            transaction.addToBackStack(null)
            transaction.commit()
        }


    }
}