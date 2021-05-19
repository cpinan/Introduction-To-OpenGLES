package com.carlospinan.openglesmedium

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var view: MyGLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = MyGLSurfaceView(MyGLRenderer(this), this)
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        if (::view.isInitialized)
            view.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (::view.isInitialized)
            view.onPause()
    }

}