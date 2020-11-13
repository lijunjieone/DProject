# 基于kotlin的自定义注解,完成自动寻找注解Fragment,加入列表能力

## 功能
  定义一个自定义注解,在某个类上加入这个注解,在运行时,自动讲标记注解的类,加入到某个列表中

# 目标
- 定义自定义注解
- 解析自定义注解
- 根据注解生成方法
- 在代码中调用生成的方法

## 定义自定义注解

```
@Target(AnnotationTarget.CLASS)
annotation class ListFragmentAnnotation(val showName: String = "")
```

## 解析自定义注解,根据注解生成方法

```

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.a.processor.ListFragmentAnnotation")
@SupportedOptions(SimpleAnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class SimpleAnnotationProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val annotatedElements =
            roundEnv.getElementsAnnotatedWith(ListFragmentAnnotation::class.java)
        if (annotatedElements.isEmpty()) return false

        val kaptKotlinGeneratedDir =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
                processingEnv.messager.printMessage(
                    ERROR,
                    "Can't find the target directory for generated Kotlin files."
                )
                return false
            }

        val generatedKtFile = kotlinFile("com.a.dproject") {

            var body = """

val map = HashMap<String,String>()
            """.trimIndent()
            for (element in annotatedElements) {
                val annotation: ListFragmentAnnotation =
                    element.getAnnotation(ListFragmentAnnotation::class.java)
                val showName = annotation.showName

                val typeElement = element.toTypeElementOrNull() ?: continue

                property("simpleClassName") {
                    receiverType(typeElement.qualifiedName.toString())
                    getterExpression("this::class.java.simpleName")
                }
                body = """
                    ${body}
map.put("$showName","${typeElement.qualifiedName.toString()}")
                """.trimIndent()

            }


            function("getAnnotationMap") {
//                param<Array<String>>("args")
                returnType("HashMap<String,String>")
                body(
                    """
${body}
return map
            """.trimIndent()
                )
            }

        }

        File(kaptKotlinGeneratedDir, "CodeGenerated.kt").apply {
            parentFile.mkdirs()
            writeText(generatedKtFile.accept(PrettyPrinter(PrettyPrinterConfiguration())))
        }

        return true
    }

    fun Element.toTypeElementOrNull(): TypeElement? {
        if (this !is TypeElement) {
            processingEnv.messager.printMessage(ERROR, "Invalid element type, class expected", this)
            return null
        }

        return this
    }
}
```

继承AbstractProcessor方法,在Process方法中可以来处理注解了
注解生成文件使用的库是takenoko,这个库没有什么文档,可以看看testcase来代码生成部分

### 配置执行类

```
在resources/META-INF.services/javax.annotation.processing.Processor的文件中列出注解处理器的类
```

## 调用生成的方法

- build.gradle 引入处理器
- 调用注解,对需要的类或者方法做标记
- 在合适的位置调用生成的方法

### build.gradle 引入处理器
app的build.gradle中增加kotlin-kapt
```
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-android-extensions'
    id 'kotlin-kapt'
}


```

依赖部分增加注解库和解析库

```
    implementation project(":annotation-processor4")
    kapt project(":annotation-processor4")

```

在代码中使用注解

```
@ListFragmentAnnotation("名字")
class EmptyFragment

```

在代码中调用注解库生成的方法

```
        getAnnotationMap().forEach {
            val key = if (TextUtils.isEmpty(it.key)) {
                it.value
            } else {
                it.key
            }
            list.add(ListDataModel(key, it.value))
        }
```

注解生成的类的位置在
```
app\build\generated\source\kaptKotlin\debug\
本例中会生成 CodeGenerated.kt

```


根据上面的生成类,我们可以看到生成了一个testGenerated.kt的getAnnotationMap()
外部可以直接调用getAnnotationMap()方法
