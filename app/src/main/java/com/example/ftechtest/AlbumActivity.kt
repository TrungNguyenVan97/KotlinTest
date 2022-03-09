package com.example.ftechtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumActivity : AppCompatActivity(), AlbumAdapter.ICallBack {

    private lateinit var rvAlbum: RecyclerView
    private val listAlbum: ArrayList<String> = arrayListOf()
    private val adapter = AlbumAdapter(listAlbum, this)

    companion object {
        const val SEND_DATA_TO_MAIN = "SEND_DATA_TO_MAIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        rvAlbum = findViewById(R.id.rv_Album)
        getTitleAlbum()
        initView()
    }

    private fun initView() {
        rvAlbum.layoutManager = GridLayoutManager(
            this,
            3,
            RecyclerView.VERTICAL,
            false
        )
        rvAlbum.adapter = adapter
    }

    private fun getTitleAlbum() {
        listAlbum.addAll(MainActivity.listBucket)
    }


    override fun onClick(position: Int) {
        if (listAlbum.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(SEND_DATA_TO_MAIN, listAlbum[position])
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}