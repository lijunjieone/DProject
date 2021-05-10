package com.bn.simpleplugin

import com.android.build.gradle.AppExtension
import javassist.*
import org.gradle.api.Plugin
import org.gradle.api.Project


class SimplePlugin : Plugin<Project> {
    override fun apply(p0: Project) {
        val appExtension = p0.extensions.getByType(
            AppExtension::class.java
        )

//        createPerson()
//        updatePerson()

        TestAsm.generatorClass()
        appExtension.registerTransform(SimpleTransform(p0))
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
//    @Throws(java.lang.Exception::class)
//    fun updatePerson() {
//        val pool = ClassPool.getDefault()
//        val cc = pool["com.a.dproject.javassist.PersonService"]
//        cc.defrost()
//        val personFly = cc.getDeclaredMethod("getName")
//        personFly.insertBefore("System.out.println(\"起飞之前准备降落伞\");")
//        personFly.insertAfter("System.out.println(\"成功落地。。。。\");")
//
//
//        //新增一个方法
//        val ctMethod = CtMethod(CtClass.voidType, "joinFriend", arrayOf(), cc)
//        ctMethod.modifiers = Modifier.PUBLIC
//        ctMethod.setBody("{System.out.println(\"i want to be your friend\");}")
//        cc.addMethod(ctMethod)
//        val person = cc.toClass().newInstance()
//        // 调用 personFly 方法
//        val personFlyMethod: Method = person.javaClass.getMethod("getName")
//        personFlyMethod.invoke(person)
//        //调用 joinFriend 方法
//        val execute: Method = person.javaClass.getMethod("joinFriend")
//        execute.invoke(person)
//    }


}