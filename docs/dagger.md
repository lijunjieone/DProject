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

