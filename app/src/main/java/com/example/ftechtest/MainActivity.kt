package com.example.ftechtest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity(), ImageAdapter.ICallBack {

    private lateinit var rvImage: RecyclerView
    private lateinit var layoutRotation: LinearLayout
    private lateinit var tvRotationTitle: TextView
    private lateinit var tvRotationPath: TextView
    private lateinit var tvRotationSize: TextView
    private lateinit var ivRotationImage: ImageView
    private val listImage: ArrayList<Image> = arrayListOf()
    private val listFolder: ArrayList<Image> = arrayListOf()
    private var adapter = ImageAdapter(listImage, this, this)
    private val grind3ColumnsLayoutManager: RecyclerView.LayoutManager by lazy {
        GridLayoutManager(
            this,
            3,
            RecyclerView.VERTICAL,
            false
        )
    }
    private val grind4ColumnsLayoutManager: RecyclerView.LayoutManager by lazy {
        GridLayoutManager(
            this,
            4,
            RecyclerView.VERTICAL,
            false
        )
    }

    private val grind5ColumnsLayoutManager: RecyclerView.LayoutManager by lazy {
        GridLayoutManager(
            this,
            5,
            RecyclerView.VERTICAL,
            false
        )
    }
    private val linearLayoutManager: RecyclerView.LayoutManager by lazy {
        LinearLayoutManager(
            this,
        )
    }

    companion object {
        const val REQUEST_CODE = 100
        const val SEND_DATA_TO_DETAILS = "SEND_DATA_TO_DETAILS"
        var checkedLayoutManager = 1
        var isCheckRotation = false
        var isCheckShowFolder = false
        var listBucket: ArrayList<String> = arrayListOf()
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val nameFolder = intent?.getStringExtra(AlbumActivity.SEND_DATA_TO_MAIN)
                showImageOfFolder(nameFolder.toString())
                isCheckShowFolder = true
            }
        }

    private fun showImageOfFolder(nameFolder: String) {
        listFolder.clear()
        for (i in 0 until listImage.size) {
            if (nameFolder == listImage[i].bucket) {
                listFolder.add(listImage[i])
            }
        }
        adapter = ImageAdapter(listFolder, this, this)
        rvImage.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findView()
        initView()
        checkPermission()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isCheckRotation = true
            layoutRotation.visibility = View.VISIBLE
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isCheckRotation = false
            layoutRotation.visibility = View.GONE
        }
    }

    override fun onClick(position: Int) {
        if (!isCheckRotation) {
            val intent = Intent(this, DetailsActivity::class.java)
            if (isCheckShowFolder) {
                intent.putExtra(SEND_DATA_TO_DETAILS, listFolder[position])
            } else {
                intent.putExtra(SEND_DATA_TO_DETAILS, listImage[position])
            }
            startActivity(intent)
        } else {
            val image: Image = if (isCheckShowFolder) {
                listFolder[position]
            } else {
                listImage[position]
            }
            tvRotationTitle.text = "Title: ${image.title} "
            tvRotationPath.text = "Path: ${image.path}"
            image.size?.let {
                tvRotationSize.text = "Size: ${it.toDouble() / 1000} KB"
            }
            Glide.with(this).load(image.path).fitCenter().into(ivRotationImage)
        }
    }

    override fun onResume() {
        super.onResume()
        checkLayoutManager()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_layoutmanager, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_Linear -> {
                rvImage.layoutManager = linearLayoutManager
                checkedLayoutManager = 1
            }
            R.id.menu_Grind3 -> {
                rvImage.layoutManager = grind3ColumnsLayoutManager
                checkedLayoutManager = 3
            }
            R.id.menu_Grind4 -> {
                rvImage.layoutManager = grind4ColumnsLayoutManager
                checkedLayoutManager = 4
            }
            R.id.menu_Grind5 -> {
                rvImage.layoutManager = grind5ColumnsLayoutManager
                checkedLayoutManager = 5
            }
            R.id.menu_Album -> {
                val intent = Intent(this, AlbumActivity::class.java)
                activityResult.launch(intent)
            }
        }
        rvImage.adapter = adapter
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getListImage()
                    initView()
                }
            }
        }
    }

    private fun checkLayoutManager() {
        when (checkedLayoutManager) {
            1 -> rvImage.layoutManager = linearLayoutManager
            3 -> rvImage.layoutManager = grind3ColumnsLayoutManager
            4 -> rvImage.layoutManager = grind4ColumnsLayoutManager
            else -> rvImage.layoutManager = grind5ColumnsLayoutManager
        }
        rvImage.adapter = adapter
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            getListImage()
        }
    }

    private fun findView() {
        rvImage = findViewById(R.id.rv_Image)
        layoutRotation = findViewById(R.id.layout_Rotation)
        tvRotationTitle = findViewById(R.id.tv_Rotation_Title)
        tvRotationPath = findViewById(R.id.tv_Rotation_Path)
        tvRotationSize = findViewById(R.id.tv_Rotation_Size)
        ivRotationImage = findViewById(R.id.iv_Rotation_Image)
    }

    private fun initView() {
        rvImage.layoutManager = linearLayoutManager
        rvImage.adapter = adapter
    }

    @SuppressLint("Range")
    private fun getListImage() {
        val projection = arrayOf(
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
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
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE))
                    val size =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                    val path =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    val bucket =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))

                    listImage.add(Image(title, size, path, bucket))

                    addAlbum(bucket)
                } while (cursor.moveToNext())
                cursor.close()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun addAlbum(bucket: String) {
        if (listBucket.isEmpty()) {
            listBucket.add(bucket)
        } else {
            if (bucket in listBucket) {
                return
            } else {
                listBucket.add(bucket)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isCheckShowFolder = false
    }
}
