package com.a.dproject.opengl;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//颜色立方体
public class Cube
{	
	int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用  
    int maColorHandle; //顶点颜色属性引用 
    String mVertexShader;//顶点着色器代码脚本  
    String mFragmentShader;//片元着色器代码脚本
	
	FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
	FloatBuffer   mColorBuffer;//顶点着色数据缓冲
    int vCount=0;

    static float[] mMMatrix = new float[16];
    float xAngle = 0;
    float yAngle = 0;

    boolean isTouch = false;



    public Cube(CubeSurfaceView mv,boolean isTouch)
    {
    	this.isTouch = isTouch;
    	//初始化顶点坐标与着色数据
    	initVertexData();
    	//初始化shader        
    	initShader(mv);

    }
    
    //初始化顶点坐标与着色数据的方法
    public void initVertexData()
    {
    	//顶点坐标数据的初始化================begin============================
        vCount=12*6; 
        
        float vertices[]=new float[]
        {
        	//前面
        	0,0,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	0,0,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	0,0,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	0,0,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	//后面
        	0,0,-Constant.CUBE_UNIT_SIZE,        	
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	0,0,-Constant.CUBE_UNIT_SIZE, 
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	0,0,-Constant.CUBE_UNIT_SIZE, 
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	0,0,-Constant.CUBE_UNIT_SIZE, 
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	//左面
        	-Constant.CUBE_UNIT_SIZE,0,0,      	
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,0,0,   
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,0,0,   
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,0,0,   
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	//右面
        	Constant.CUBE_UNIT_SIZE,0,0,   
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,0,0,   
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,0,0,   
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,0,0,  
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	//上面
        	0,Constant.CUBE_UNIT_SIZE,0,      
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	0,Constant.CUBE_UNIT_SIZE,0,        	
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	0,Constant.CUBE_UNIT_SIZE,0,       
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE, 	
        	0,Constant.CUBE_UNIT_SIZE,0,      
        	-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,  	
        	//下面
        	0,-Constant.CUBE_UNIT_SIZE,0,        	
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	0,-Constant.CUBE_UNIT_SIZE,0,  
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	0,-Constant.CUBE_UNIT_SIZE,0,   
        	-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	0,-Constant.CUBE_UNIT_SIZE,0,    
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,
        	Constant.CUBE_UNIT_SIZE,-Constant.CUBE_UNIT_SIZE,Constant.CUBE_UNIT_SIZE,
        };
        
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================
       
    	//顶点颜色值数组，每个顶点4个色彩值RGBA
        float colors[]=new float[]{
        		//前面        
        		1,1,1,0,//中间为白色
        		1,0,0,0,
        		1,0,0,0,
        		1,1,1,0,//中间为白色
        		1,0,0,0,
        		1,0,0,0,
        		1,1,1,0,//中间为白色
        		1,0,0,0,
        		1,0,0,0,
        		1,1,1,0,//中间为白色
        		1,0,0,0,
        		1,0,0,0,
        		//后面
        		1,1,1,0,//中间为白色
        		0,0,1,0,
        		0,0,1,0, 
        		1,1,1,0,//中间为白色
        		0,0,1,0,
        		0,0,1,0, 
        		1,1,1,0,//中间为白色
        		0,0,1,0,
        		0,0,1,0, 
        		1,1,1,0,//中间为白色
        		0,0,1,0,
        		0,0,1,0, 
        		//左面
        		1,1,1,0,//中间为白色
        		1,0,1,0,
        		1,0,1,0, 
        		1,1,1,0,//中间为白色
        		1,0,1,0,
        		1,0,1,0, 
        		1,1,1,0,//中间为白色
        		1,0,1,0,
        		1,0,1,0, 
        		1,1,1,0,//中间为白色
        		1,0,1,0,
        		1,0,1,0, 
        		//右面
        		1,1,1,0,//中间为白色
        		1,1,0,0,
        		1,1,0,0,
        		1,1,1,0,//中间为白色
        		1,1,0,0,
        		1,1,0,0,
        		1,1,1,0,//中间为白色
        		1,1,0,0,
        		1,1,0,0,
        		1,1,1,0,//中间为白色
        		1,1,0,0,
        		1,1,0,0,
        		//上面
        		1,1,1,0,//中间为白色
        		0,1,0,0,
        		0,1,0,0,
        		1,1,1,0,//中间为白色
        		0,1,0,0,
        		0,1,0,0,
        		1,1,1,0,//中间为白色
        		0,1,0,0,
        		0,1,0,0,
        		1,1,1,0,//中间为白色
        		0,1,0,0,
        		0,1,0,0,        		
        		//下面
        		1,1,1,0,//中间为白色
        		0,1,1,0,
        		0,1,1,0,
        		1,1,1,0,//中间为白色
        		0,1,1,0,
        		0,1,1,0,
        		1,1,1,0,//中间为白色
        		0,1,1,0,
        		0,1,1,0,
        		1,1,1,0,//中间为白色
        		0,1,1,0,
        		0,1,1,0,
        };
        //创建顶点着色数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mColorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mColorBuffer.put(colors);//向缓冲区中放入顶点着色数据
        mColorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================
    }
    //初始化shader
    public void initShader(CubeSurfaceView mv)
    {
    	//加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用id  
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix"); 
    }
    
    public void drawSelf()
    {        
    	 //制定使用某套shader程序
    	 GLES30.glUseProgram(mProgram);

		//初始化变换矩阵
		Matrix.setRotateM(mMMatrix,0,0,0,1,0);
		//设置沿Z轴正向位移1
		Matrix.translateM(mMMatrix,0,0,0,1);
		//设置绕y轴旋转yAngle度
		Matrix.rotateM(mMMatrix,0,yAngle,0,1,0);
		//设置绕x轴旋转xAngle度
		Matrix.rotateM(mMMatrix,0,xAngle,1,0,0);

		if(!isTouch){
			//将最终变换矩阵传入shader程序
			GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		}else {
			GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMMatrix), 0);

		}

		//为画笔指定顶点位置数据
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //为画笔指定顶点着色数据
         GLES30.glVertexAttribPointer  
         (
        		maColorHandle, 
         		4, 
         		GLES30.GL_FLOAT, 
         		false,
                4*4,   
                mColorBuffer
         );   
         //允许顶点位置数据数组
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maColorHandle);  
         //绘制立方体         
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0, vCount); 
    }
}