package com.carlospinan.openglesmedium.objects

import android.content.Context
import android.opengl.GLES32
import com.carlospinan.openglesmedium.R
import com.carlospinan.openglesmedium.common.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

/**
 * @author Carlos Piñan
 */
private const val COORDS_PER_VERTEX = 3
private const val COLORS_PER_VERTEX = 4

private const val VERTEX_STRIDE = COORDS_PER_VERTEX * FLOAT_BYTES
private const val COLOR_STRIDE = COLORS_PER_VERTEX * FLOAT_BYTES

private val vertex = floatArrayOf(
    // Front Face
    -1F, -1F, 1F,
    1F, -1F, 1F,
    1F, 1F, 1F,
    -1F, 1F, 1F,

    // Back Face
    -1F, -1F, -1F,
    -1F, 1F, -1F,
    1F, 1F, -1F,
    1F, -1F, -1F,

    // Top Face
    -1F, 1F, -1F,
    -1F, 1F, 1F,
    1F, 1F, 1F,
    1F, 1F, -1F,

    // Bottom Face
    -1F, -1F, -1F,
    1F, -1F, -1F,
    1F, -1F, 1F,
    -1F, -1F, 1F,

    // Right
    1F, -1F, -1F,
    1F, 1F, -1F,
    1F, 1F, 1F,
    1F, -1F, 1F,

    // Left Face
    -1F, -1F, -1F,
    -1F, -1F, 1F,
    -1F, 1F, 1F,
    -1F, 1F, -1F
)

private val indices = intArrayOf(
    0, 1, 2, 0, 2, 3, // front face
    4, 5, 6, 4, 6, 7, // back face
    8, 9, 10, 8, 10, 11, // top face
    12, 13, 14, 12, 14, 15, // bottom face
    16, 17, 18, 16, 18, 19, // right face
    20, 21, 22, 20, 22, 23 // left face
)

private val colors = floatArrayOf(
    // Front Face
    0F, 0F, 1F, 1F,
    0F, 0F, 1F, 1F,
    0F, 0F, 1F, 1F,
    0F, 0F, 1F, 1F,
    // Back Face
    0F, 1F, 0F, 1F,
    0F, 1F, 0F, 1F,
    0F, 1F, 0F, 1F,
    0F, 1F, 0F, 1F,
    // Top Face
    1F, 0F, 0F, 1F,
    1F, 0F, 0F, 1F,
    1F, 0F, 0F, 1F,
    1F, 0F, 0F, 1F,
    // Bottom Face
    0F, 1F, 1F, 1F,
    0F, 1F, 1F, 1F,
    0F, 1F, 1F, 1F,
    0F, 1F, 1F, 1F,
    // Right Face
    1F, 1F, 0F, 1F,
    1F, 1F, 0F, 1F,
    1F, 1F, 0F, 1F,
    1F, 1F, 0F, 1F,
    // Left Face
    1F, 0F, 1F, 1F,
    1F, 0F, 1F, 1F,
    1F, 0F, 1F, 1F,
    1F, 0F, 1F, 1F
)

class Cube(context: Context) {

    private var program =
        createProgram(context, R.raw.color_vertex_shader, R.raw.common_fragment_shader)

    private val attributePositionHandle by lazy {
        GLES32.glGetAttribLocation(program, A_VERTEX_POSITION)
    }

    private val attributeColorHandle by lazy {
        GLES32.glGetAttribLocation(program, A_VERTEX_COLOR)
    }

    private val uniformMvpMatrix by lazy {
        GLES32.glGetUniformLocation(program, U_MVP_MATRIX)
    }

    private val vertexBuffer = ByteBuffer
        .allocateDirect(vertex.size * FLOAT_BYTES)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .apply {
            put(vertex)
            position(0)
        }

    private val colorBuffer = ByteBuffer
        .allocateDirect(colors.size * FLOAT_BYTES)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .apply {
            put(colors)
            position(0)
        }

    private val orderBuffer = IntBuffer.allocate(indices.size)
        .apply {
            put(indices)
            position(0)
        }

    fun draw(modelViewProjectionMatrix: FloatArray) {
        with(program) {
            GLES32.glUseProgram(this)

            GLES32.glEnableVertexAttribArray(attributePositionHandle)
            GLES32.glEnableVertexAttribArray(attributeColorHandle)

            GLES32.glUniformMatrix4fv(
                uniformMvpMatrix,
                1,
                false,
                modelViewProjectionMatrix,
                0
            )

            GLES32.glVertexAttribPointer(
                attributePositionHandle,
                COORDS_PER_VERTEX,
                GLES32.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                vertexBuffer
            )

            GLES32.glVertexAttribPointer(
                attributeColorHandle,
                COLORS_PER_VERTEX,
                GLES32.GL_FLOAT,
                false,
                COLOR_STRIDE,
                colorBuffer
            )

            GLES32.glDrawElements(
                GLES32.GL_TRIANGLES,
                indices.size,
                GLES32.GL_UNSIGNED_INT,
                orderBuffer
            )

            GLES32.glDisableVertexAttribArray(attributePositionHandle)
            GLES32.glDisableVertexAttribArray(attributeColorHandle)

            GLES32.glUseProgram(0)
        }
    }

}