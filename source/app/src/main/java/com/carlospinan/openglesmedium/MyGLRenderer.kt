package com.carlospinan.openglesmedium

import android.content.Context
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.carlospinan.openglesmedium.objects.SquareV1
import com.carlospinan.openglesmedium.objects.SquareV2
import com.carlospinan.openglesmedium.objects.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author Carlos Piñan
 */
class MyGLRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    private lateinit var triangle: Triangle
    private lateinit var squareV1: SquareV1
    private lateinit var squareV2: SquareV2

    private val projectionMatrix = FloatArray(16)

    private val viewMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)
    private val modelViewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private var z = -1F
    private var decrease = true

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        triangle = Triangle(context)
        squareV1 = SquareV1(context)
        squareV2 = SquareV2(context)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // glViewport specifies the affine transformation of x and y from
        // normalized device coordinates to window coordinates.
        // Let x nd y nd be normalized device coordinates.
        // Then the window coordinates x w y w are computed as follows:
        GLES32.glViewport(0, 0, width, height)

        val aspectRatio = width.toFloat() / height.toFloat()

        Matrix.frustumM(
            projectionMatrix,
            0,
            -aspectRatio,
            aspectRatio,
            -1F,
            1F,
            1F,
            10F
        )

    }

    override fun onDrawFrame(gl: GL10?) {
        GLES32.glClearColor(0F, 0F, 0F, 1F)
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        // Initialize
        Matrix.setIdentityM(modelViewProjectionMatrix, 0)
        Matrix.setIdentityM(modelViewMatrix, 0)
        Matrix.setIdentityM(modelMatrix, 0)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(
            viewMatrix,
            0,
            0F,
            0F,
            1F,
            0F,
            0F,
            0F,
            0F,
            1F,
            0F
        )

        z += 0.01F * if (decrease) -1 else 1

        if (z < -3F) {
            decrease = !decrease
            z = -3F
        } else if (z > -1F) {
            decrease = !decrease
            z = -1F
        }

        Matrix.translateM(
            modelMatrix,
            0,
            0F,
            0F,
            z
        )

        Matrix.multiplyMM(
            modelViewMatrix,
            0,
            viewMatrix,
            0,
            modelMatrix,
            0
        )

        Matrix.multiplyMM(
            modelViewProjectionMatrix,
            0,
            projectionMatrix,
            0,
            modelViewMatrix,
            0
        )

        // triangle.draw(modelViewProjectionMatrix)
        // squareV1.draw(modelViewProjectionMatrix)
        squareV2.draw(modelViewProjectionMatrix)
    }
}