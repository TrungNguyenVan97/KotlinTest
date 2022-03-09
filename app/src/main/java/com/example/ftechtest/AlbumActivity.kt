package com.example.ftechtest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumActivity : AppCompatActivity(), ImageAdapter.ICallBack {

    private lateinit var rvAlbum: RecyclerView
    private val listAlbum: ArrayList<Album> = arrayListOf()
    private val adapter = AlbumAdapter(listAlbum, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        rvAlbum = findViewById(R.id.rv_Album)
        getAlum()
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

    @SuppressLint("Range")
    private fun getAlum() {
        val projection = arrayOf(
            MediaStore.Images.Media.ALBUM,
        )

        val cursor = this.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.ALBUM))
                    listAlbum.add(Album(title))
                } while (cursor.moveToNext())
                cursor.close()
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(position: Int) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}