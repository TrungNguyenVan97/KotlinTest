package com.example.ftechtest

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvPath: TextView
    private lateinit var tvSize: TextView
    private lateinit var ivImage: ImageView
    private lateinit var ivBack: ImageView
    private lateinit var image: Image
    private lateinit var cbLike: CheckBox
    private lateinit var wallPaper: WallpaperManager

    companion object {
        var listFavorite: ArrayList<Image> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        findView()
        receiveDataFromMainActivity()
        initAction()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meuu_share_setbackground, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_Share -> shareImage()
            R.id.menu_SetBackGround -> setBackGround()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBackGround() {
        wallPaper = WallpaperManager.getInstance(this)
        val bitmap: Bitmap = ivImage.drawable.toBitmap()
        try {
            wallPaper.setBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        val uri = Uri.parse(image.path)
        intent.type = "image/all"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, "share"))
    }

    private fun initAction() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
        cbLike.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    listFavorite.add(image)
                }
                else -> {
                    if (image in listFavorite) {
                        listFavorite.remove(image)
                    }
                }
            }
        }
    }

    private fun receiveDataFromMainActivity() {
        image = intent.getSerializableExtra(MainActivity.SEND_DATA_TO_DETAILS) as Image
        tvTitle.text = "Title: ${image.title} "
        tvPath.text = "Path: ${image.path}"
        tvSize.text = "Size: ${image.size.toDouble() / 1000} KB"
        Glide.with(this).load(image.path).fitCenter().into(ivImage)
        cbLike.isChecked = image in listFavorite
    }

    private fun findView() {
        tvTitle = findViewById(R.id.tv_Details_Title)
        tvPath = findViewById(R.id.tv_Details_Path)
        tvSize = findViewById(R.id.tv_Details_Size)
        ivImage = findViewById(R.id.iv_Details_Image)
        ivBack = findViewById(R.id.iv_Details_Back)
        cbLike = findViewById(R.id.cb_Details_Like)
    }
}