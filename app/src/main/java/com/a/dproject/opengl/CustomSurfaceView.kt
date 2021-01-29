package com.a.dproject.opengl

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class CustomSurfaceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        GLSurfaceView(context, attrs ){
    private lateinit var mRenderer: SceneRenderer


    fun initRender(smallRenderer: SmallRenderer) {
        this.smallRenderer = smallRenderer
        mRenderer = SceneRenderer()
        mRenderer.surfaceView = this
        setRenderer(mRenderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    interface SmallRenderer {
        open fun onDrawFrame(gl:GL10)
        open fun onSurfaceChanged(gl: GL10, width: Int, height: Int)
        open fun onSurfaceCreated(gl: GL10, config: EGLConfig)
    }

    lateinit var smallRenderer: SmallRenderer


    class SceneRenderer : Renderer {
        var belt //条状物
                : Belt? = null
        var circle //圆
                : Circle? = null

        lateinit var surfaceView: CustomSurfaceView

        override fun onDrawFrame(gl: GL10) {
            //清除深度缓冲与颜色缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            surfaceView.smallRenderer.onDrawFrame(gl)
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            //设置视口的大小及位置
            GLES30.glViewport(0, 0, width, height)
            //计算视口的宽高比
            Constant.ratio = width.toFloat() / height
            // 调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1f, 1f, 20f, 100f)
            // 调用此方法产生摄像机矩阵
            MatrixState.setCamera(0f, 8f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

            //初始化变换矩阵
            MatrixState.setInitStack()
        }

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
            surfaceView.smallRenderer.onSurfaceCreated(gl,config)
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            //打开背面剪裁
            GLES30.glEnable(GLES30.GL_CULL_FACE)
        }
    }


}

