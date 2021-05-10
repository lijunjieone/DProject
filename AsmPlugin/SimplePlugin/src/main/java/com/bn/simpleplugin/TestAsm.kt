package com.bn.simpleplugin

import com.kronos.plugin.base.ClassUtils
import org.objectweb.asm.ClassWriter
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
    }

    private fun toByteArray(cn: ClassNode): ByteArray? {
        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
        cn.accept(cw)
        return cw.toByteArray()
    }
}