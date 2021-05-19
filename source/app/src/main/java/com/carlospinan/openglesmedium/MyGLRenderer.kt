package com.carlospinan.openglesmedium

import android.content.Context
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

/**
 * @author Carlos Piñan
 */
class MyGLRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES32.glClearColor(0F, 0F, 0F, 1F)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // glViewport specifies the affine transformation of x and y from
        // normalized device coordinates to window coordinates.
        // Let x nd y nd be normalized device coordinates.
        // Then the window coordinates x w y w are computed as follows:
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT)

        val r = Random.nextFloat()
        val g = Random.nextFloat()
        val b = Random.nextFloat()

        GLES32.glClearColor(r, g, b, 1F)
    }
}