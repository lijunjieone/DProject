package com.bn.simpleplugin

import com.kronos.plugin.base.ClassUtils
import javassist.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode
import java.io.File


object TestAsm {
    fun generatorClass(){
        val cn = ClassNode()
        cn.version = V1_5
        cn.access = ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE
        cn.name = "pkg/Comparable"
        cn.superName = "java/lang/Object"
        cn.interfaces.add("pkg/Mesurable")
        cn.fields.add(FieldNode(ACC_PUBLIC+ ACC_FINAL+ ACC_STATIC,"LESS","I",null,-1))
        cn.fields.add(FieldNode(ACC_PUBLIC+ ACC_FINAL+ ACC_STATIC,"EQUAL","I",null,0))
        cn.fields.add(FieldNode(ACC_PUBLIC+ ACC_FINAL+ ACC_STATIC,"GREATER","I",null,1))
        cn.methods.add(MethodNode(ACC_PUBLIC+ ACC_ABSTRACT,"compareTo","(Ljava/lang/Object;)I",null,null))
//        val classF = "D:\\dev\\studio\\DProject\\app\\build\\intermediates\\transforms\\SimpleTransform\\debug\\111\\temp\\com\\a\\dproject\\ar\\fragment\\CustomArCoreFragment.class"
        val classF2 = "D:\\dev\\studio\\DProject\\app\\build\\C.class"
        ClassUtils.saveFile(File(classF2), toByteArray(cn))
        val classF3 = "D:\\dev\\studio\\DProject\\app\\build\\D.class"
        val class1 = createClass("A")
        ClassUtils.saveFile(File(classF3), toByteArray(class1!!))
    }

    private fun createClass(className: String): ClassNode? {
        val classNode = ClassNode()
        classNode.visit(49, ACC_SUPER + ACC_PUBLIC, className, null, "java/lang/Object", null)
        val mv: MethodVisitor = classNode.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        mv.visitCode()
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(RETURN)
        mv.visitMaxs(1, 1)
        mv.visitEnd()
        classNode.visitEnd()
        return classNode
    }


    private fun toByteArray(cn: ClassNode): ByteArray? {
        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
        cn.accept(cw)
        return cw.toByteArray()
    }
    fun needHandle(className:String):Boolean {
        System.out.println(className)
        return className.contains("PersonService")
    }

    fun handleTestClass(srcClass: ByteArray): ByteArray {
        val classNode = ClassNode(ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)
        val classWriter = ClassWriter(0)
        return classWriter.toByteArray()
    }


    @Throws(Exception::class)
    fun createPerson() {
        val pool = ClassPool.getDefault()

        // 1. 创建一个空类
        val cc = pool.makeClass("com.a.dproject.javassist.Person")
        cc.defrost()

        // 2. 新增一个字段 private String name;
        // 字段名为name
        val param = CtField(pool["java.lang.String"], "name", cc)
        // 访问级别是 private
        param.modifiers = Modifier.PRIVATE
        // 初始值是 "xiaoming"
        cc.addField(param, CtField.Initializer.constant("xiaoming"))

        // 3. 生成 getter、setter 方法
        cc.addMethod(CtNewMethod.setter("setName", param))
        cc.addMethod(CtNewMethod.getter("getName", param))

        // 4. 添加无参的构造函数
        var cons = CtConstructor(arrayOf(), cc)
        cons.setBody("{name = \"xiaohong\";}")
        cc.addConstructor(cons)

        // 5. 添加有参的构造函数
        cons = CtConstructor(arrayOf(pool["java.lang.String"]), cc)
        // $0=this / $1,$2,$3... 代表方法参数
        cons.setBody("{$0.name = $1;}")
        cc.addConstructor(cons)

        // 6. 创建一个名为printName方法，无参数，无返回值，输出name值
        val ctMethod = CtMethod(CtClass.voidType, "printName", arrayOf(), cc)
        ctMethod.modifiers = Modifier.PUBLIC
        ctMethod.setBody("{System.out.println(name);}")
        cc.addMethod(ctMethod)

//        D:\dev\studio\DProject\app\src\main\java\com\a\dproject\javassist\PersonService.java
        //这里会将这个创建的类对象编译为.class文件
        cc.writeFile("D:\\dev\\studio\\DProject\\app\\src\\main\\java\\")
    }

    @Throws(java.lang.Exception::class)
    fun updatePerson() {
        val pool = ClassPool.getDefault()
        pool.appendClassPath("D:\\dev\\studio\\DProject\\app\\build\\intermediates\\javac\\debug\\classes")
        val cc = pool.get("com.a.dproject.javassist.PersonService")
//        val cc = pool["com.a.dproject.javassist.PersonService"]
        cc.defrost()
        val personFly = cc.getDeclaredMethod("personFly")
        personFly.insertBefore("System.out.println(\"起飞之前准备降落伞\");")
        personFly.insertAfter("System.out.println(\"成功落地。。。。\");")


        //新增一个方法
        val ctMethod = CtMethod(CtClass.voidType, "joinFriend", arrayOf(), cc)
        ctMethod.modifiers = Modifier.PUBLIC
        ctMethod.setBody("{System.out.println(\"i want to be your friend\");}")
        cc.addMethod(ctMethod)
        val person = cc.toClass().newInstance()
        // 调用 personFly 方法
        val personFlyMethod = person.javaClass.getMethod("personFly")
        personFlyMethod.invoke(person)
        //调用 joinFriend 方法
        val execute = person.javaClass.getMethod("joinFriend")
        execute.invoke(person)
    }


}