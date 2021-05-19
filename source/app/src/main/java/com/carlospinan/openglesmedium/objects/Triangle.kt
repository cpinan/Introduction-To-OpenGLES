package com.carlospinan.openglesmedium.objects

import android.content.Context
import android.opengl.GLES32
import com.carlospinan.openglesmedium.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author Carlos Piñan
 */
private const val COORDS_PER_VERTEX = 3
private const val VERTEX_STRIDE = COORDS_PER_VERTEX * FLOAT_BYTES
private const val A_VERTEX_POSITION = "aVertexPosition"
private const val U_MVP_MATRIX = "uMVPMatrix"

private val triangleVertex = floatArrayOf(
    -1F, -1F, 0F,
    1F, -1F, 0F,
    0F, 1F, 0F
)

class Triangle(
    context: Context
) {

    private var program =
        createProgram(context, R.raw.triangle_vertex_shader, R.raw.triangle_fragment_shader)

    private val vertexBuffer: FloatBuffer =
        ByteBuffer
            .allocateDirect(triangleVertex.size * FLOAT_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(triangleVertex)
                position(0)
            }

    private val vertexCount = triangleVertex.size / COORDS_PER_VERTEX
    private var positionHandle = 0
    private var mvpMatrixHandle = 0

    fun draw(modelViewProjectionMatrix: FloatArray) {
        with(program) {
            GLES32.glUseProgram(this)
            checkGLError("glUserProgram in Triangle.kt")

            positionHandle = GLES32.glGetAttribLocation(this, A_VERTEX_POSITION)
            checkGLError("positionHandle = $positionHandle in Triangle.kt")

            // Enable a handle to the triangle vertices
            GLES32.glEnableVertexAttribArray(positionHandle)
            checkGLError("glEnableVertexAttribArray in Triangle.kt")

            mvpMatrixHandle = GLES32.glGetUniformLocation(this, U_MVP_MATRIX)
            checkGLError("mvpMatrixHandle = $mvpMatrixHandle in Triangle.kt")

            GLES32.glUniformMatrix4fv(
                mvpMatrixHandle,
                1,
                false,
                modelViewProjectionMatrix,
                0
            )
            checkGLError("glUniformMatrix4fv in Triangle.kt")

            GLES32.glVertexAttribPointer(
                positionHandle,
                COORDS_PER_VERTEX,
                GLES32.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                vertexBuffer
            )
            checkGLError("glVertexAttribPointer in Triangle.kt")

            GLES32.glDrawArrays(
                GLES32.GL_TRIANGLES,
                0,
                vertexCount
            )
            checkGLError("glDrawArrays in Triangle.kt")

            GLES32.glDisableVertexAttribArray(positionHandle)
            checkGLError("glDisableVertexAttribArray in Triangle.kt")

            GLES32.glUseProgram(0)
        }
    }

}