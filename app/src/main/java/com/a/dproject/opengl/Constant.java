package com.a.dproject.opengl;

public class Constant 
{
	//单位尺寸
	public static final float UNIT_SIZE=1f;
	public static final float CUBE_UNIT_SIZE=0.5f;
	//计算GLSurfaceView的宽高比
    public static float ratio;

	//绘制方式
	public static final int GL_POINTS = 0;
	public static final int GL_LINES = 1;
	public static final int GL_LINE_STRIP = 2;
	public static final int GL_LINE_LOOP = 3;

	public static int CURR_DRAW_MODE = 1;//当前绘制方式
}
