package com.a.dproject.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView


class MySurfaceView : SurfaceView, Runnable, SurfaceHolder.Callback {

    constructor(ctx: Context) : super(ctx)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mHolder: SurfaceHolder
    private var t // 声明一条线程
            : Thread? = null
    private var bitmap:Bitmap?=null

    @Volatile
    var flag // 线程运行的标识，用于控制线程
            = false
    private var mCanvas // 声明一张画布
            : Canvas? = null
    private var p // 声明一支画笔
            : Paint? = null
    var m_circle_r = 10f

    init {
        mHolder = holder // 获得SurfaceHolder对象

        mHolder.addCallback(this) // 为SurfaceView添加状态监听

        p = Paint() // 创建一个画笔对象

        p?.setColor(Color.WHITE) // 设置画笔的颜色为白色

        isFocusable = true // 设置焦点


    }

    override fun run() {
        while (flag) {
            try {
                synchronized(mHolder) {
                    Thread.sleep(100) // 让线程休息100毫秒
                    drawSomething() // 调用自定义画画方法
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                if (mCanvas != null) {
                    // mHolder.unlockCanvasAndPost(mCanvas);//结束锁定画图，并提交改变。
                }
            }
        }
    }

    fun snapshot(width:Int,height:Int): Bitmap {

        val myBitmap = Bitmap.createBitmap(width , height , Bitmap.Config.RGB_565)
        mCanvas?.let{
            val baseRect = Rect(0, 0, width, height)
            it.drawBitmap(myBitmap,baseRect,baseRect,null)
        }


        return myBitmap
    }

    private fun drawSomething() {
        mCanvas = mHolder.lockCanvas() // 获得画布对象，开始对画布画画

        if (mCanvas != null) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.BLUE
            paint.strokeWidth = 10f
            paint.style = Paint.Style.FILL
            if (m_circle_r >= width / 10) {
                m_circle_r = 0f
            } else {
                m_circle_r++
            }
//            val pic = (resources.getDrawable(
//                    R.drawable.qq) as BitmapDrawable).bitmap
//            mCanvas?.drawBitmap(pic, 0f, 0f, paint)
            for (i in 0..4) for (j in 0..7) mCanvas?.drawCircle((
                    width / 5 * i + width / 10).toFloat(), (
                    height / 8 * j + height / 16).toFloat(),
                    m_circle_r, paint)
            mHolder.unlockCanvasAndPost(mCanvas) // 完成画画，把画布显示在屏幕上
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        t = Thread(this); // 创建一个线程对象
        flag = true; // 把线程运行的标识设置成true
        t?.start(); // 启动线程
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        flag = false; // 把线程运行的标识设置成false
        mHolder.removeCallback(this);
    }

}