# kotlin使用自定义注解

# 目标
- 定义自定义注解
- 解析自定义注解
- 根据注解生成方法
- 在代码中调用生成的方法

## 定义自定义注解

```
@Target(AnnotationTarget.CLASS)
annotation class ListFragmentAnnotation

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

        val generatedKtFile = kotlinFile("test.generated") {

            var body = """

val l = ArrayList<String>()
            """.trimIndent()
            for (element in annotatedElements) {
                val typeElement = element.toTypeElementOrNull() ?: continue

                property("simpleClassName") {
                    receiverType(typeElement.qualifiedName.toString())
                    getterExpression("this::class.java.simpleName")
                }
                body = """
                    ${body}
l.add("${typeElement.qualifiedName.toString()}")
                """.trimIndent()

            }


            function("getShowList") {
//                param<Array<String>>("args")
                returnType("ArrayList<String>")
                body(
                    """
${body}
return l
            """.trimIndent()
                )
            }

        }

        File(kaptKotlinGeneratedDir, "testGenerated.kt").apply {
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

## 调用生成的方法

根据上面的生成类,我们可以看到生成了一个testGenerated.kt的getShowList()
外部可以直接调用getShowList()方法