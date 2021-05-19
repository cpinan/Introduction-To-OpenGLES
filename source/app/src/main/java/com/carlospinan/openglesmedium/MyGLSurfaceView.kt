package com.carlospinan.openglesmedium

import android.content.Context
import android.opengl.GLSurfaceView

/**
 * @author Carlos Piñan
 */
class MyGLSurfaceView(
    renderer: Renderer,
    context: Context
) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(3)
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }
}