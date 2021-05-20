package com.carlospinan.openglesmedium.objects

import android.content.Context
import android.opengl.GLES32
import com.carlospinan.openglesmedium.R
import com.carlospinan.openglesmedium.common.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

private val square1Vertex = floatArrayOf(
    -1F, -1F, 1F,
    -1F, 1F, 1F,
    1F, 1F, 1F,

    1F, 1F, 1F,
    1F, -1F, 1F,
    -1F, -1F, 1F
)

private val square2Vertex = floatArrayOf(
    -1F, -1F, 1F, // 0
    -1F, 1F, 1F, // 1
    1F, 1F, 1F, // 2
    1F, -1F, 1F // 3
)

private val square2IndexOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

private const val COORDS_PER_VERTEX = 3
private const val VERTEX_STRIDE = COORDS_PER_VERTEX * FLOAT_BYTES

/**
 * @author Carlos Piñan
 */
class SquareV1(
    context: Context
) {
    private var program =
        createProgram(context, R.raw.common_vertex_shader, R.raw.common_fragment_shader)

    private val vertexBuffer: FloatBuffer =
        ByteBuffer
            .allocateDirect(square1Vertex.size * FLOAT_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(square1Vertex)
                position(0)
            }

    private val vertexCount = square1Vertex.size / COORDS_PER_VERTEX
    private var positionHandle = 0
    private var mvpMatrixHandle = 0

    fun draw(modelViewProjectionMatrix: FloatArray) {
        with(program) {
            GLES32.glUseProgram(this)

            positionHandle = GLES32.glGetAttribLocation(this, A_VERTEX_POSITION)

            // Enable a handle to the triangle vertices
            GLES32.glEnableVertexAttribArray(positionHandle)

            mvpMatrixHandle = GLES32.glGetUniformLocation(this, U_MVP_MATRIX)

            GLES32.glUniformMatrix4fv(
                mvpMatrixHandle,
                1,
                false,
                modelViewProjectionMatrix,
                0
            )

            GLES32.glVertexAttribPointer(
                positionHandle,
                COORDS_PER_VERTEX,
                GLES32.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                vertexBuffer
            )

            GLES32.glDrawArrays(
                GLES32.GL_TRIANGLES,
                0,
                vertexCount
            )

            GLES32.glDisableVertexAttribArray(positionHandle)
            GLES32.glUseProgram(0)
        }
    }

}

class SquareV2(
    context: Context
) {
    private var program =
        createProgram(context, R.raw.common_vertex_shader, R.raw.common_fragment_shader)

    private val vertexBuffer: FloatBuffer =
        ByteBuffer
            .allocateDirect(square2Vertex.size * FLOAT_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(square2Vertex)
                position(0)
            }

    private val listBuffer: ShortBuffer =
        ByteBuffer
            .allocateDirect(square2IndexOrder.size * INT_BYTES)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .apply {
                put(square2IndexOrder)
                position(0)
            }

    private var positionHandle = 0
    private var mvpMatrixHandle = 0

    fun draw(modelViewProjectionMatrix: FloatArray) {
        with(program) {
            GLES32.glUseProgram(this)

            positionHandle = GLES32.glGetAttribLocation(this, A_VERTEX_POSITION)

            // Enable a handle to the triangle vertices
            GLES32.glEnableVertexAttribArray(positionHandle)

            mvpMatrixHandle = GLES32.glGetUniformLocation(this, U_MVP_MATRIX)

            GLES32.glUniformMatrix4fv(
                mvpMatrixHandle,
                1,
                false,
                modelViewProjectionMatrix,
                0
            )

            GLES32.glVertexAttribPointer(
                positionHandle,
                COORDS_PER_VERTEX,
                GLES32.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                vertexBuffer
            )

            GLES32.glDrawElements(
                GLES32.GL_TRIANGLES,
                square2IndexOrder.size,
                GLES32.GL_UNSIGNED_SHORT,
                listBuffer
            )

            GLES32.glDisableVertexAttribArray(positionHandle)
            GLES32.glUseProgram(0)
        }
    }

}