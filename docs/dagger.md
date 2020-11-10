# Dagger2的学习
注解关键字
- Inject
- Component

[参考资料](https://zhuanlan.zhihu.com/p/24454466)
例子在com.a.dproject.dragger中,对象包括Car Engine CarComponent
Car中包含Engine类
## 做法A
在Car中定义Engine,调用Engine相关功能

## 做法B
在Car中增加Engine设置方法,通过外部传入Engine来使用相关功能

## 做法C
在Car中的变量中增加@Inject标记.来表示这个变量将通过Dagger传入,
在Car中调用DaggerCarCommonent生成类,来完成Engine的注入,
DaggerCarCommpent是CarCommpent的一个Dagger实现.

Kodein是kotlin版本的Dagger,不是使用注解,不过不管是Dagger2还是Kodein都是解决的以来注入,主要是解耦.带来的问题也是更难理解.因为你看到的都是他们的简化写法,这个对象的创建
,以及创建了几个对象都是隐含在定义之中.所以其实对于移动开发,感觉有点大刀小苹果了.其实可以聚焦在减少代码量,也更容易理解的方面下手.