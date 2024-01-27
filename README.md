# spring-bean-timestatistics

## 当前问题

很多业务项目启动 Spring 容器慢，那么我们就需要知道启动到底慢在哪里，结合 Spring 的生命周期，就需要去统计每个 Bean 实例化的时间，有几种很常见的切入点：

* 基于 `BeanPostProcessor` 

  `BeanPostProcessor`  提供了两个函数可以帮助我们在 Bean 实例化之后，属性赋值前做一些事情，这也是网上很多博客介绍的一种方案，但是这个方案有个细节要注意，一个 Bean 可能被多个 `BeanPostProcessor` 去处理，所以要注意计算耗时的 `BeanPostProcessor` 的`postProcessBeforeInitialization` 是第一个调用，且 `postProcessAfterInitialization` 是最后一个调用。这里就涉及到多个 `BeanPostProcessor` 处理的顺序问题。

  在 Spring 容器中，多个  `BeanPostProcessor` ，它们的执行顺序是：

  1. `postProcessBeforeInitialization` 方法：按照   `BeanPostProcessor`  在 Spring 容器中的注册顺序依次执行。
  2. `InitializingBean.afterPropertiesSet` 方法：这是Bean初始化后调用的方法。
  3. Bean 配置中的 `init-method`：如果有配置，则执行该方法。
  4. `postProcessAfterInitialization` 方法：按照   `BeanPostProcessor`  在 Spring 容器中的注册顺序依次执行。

​		很明显，与我们期望的计算耗时的 `BeanPostProcessor` 的 `postProcessBeforeInitialization`  和 `postProcessAfterInitialization` 的执行顺序是有冲突的，所以需要特殊处理。当然如果实际场景影响不大，直接基于 `BeanPostProcessor`  来做问题也不大。

* 基于 `InstantiationStrategy`

  它负责基于默认构造器，实例化 Bean。需要在 `instantiate` 函数执行前后进行统计，但整体实现相对比较复杂

* 基于 `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean`

  统计的时间范围太宽了，属性赋值的情况也会计入其中，不太准确

* 其实以上三点皆有缺陷，在业务系统中，每个 Bean 的耗时点可能各不一样，有的在实例化阶段，有的在初始化阶段（初始化又分多种情况，如 `@PostConstruct`、`afterPropertiesSet`，`init-method`）。所以这几块要单独统计，便于真正知道 Bean 初始化的耗时

## 设计实现

[优化单元测试效率：Spring 工程启动耗时统计](https://mp.weixin.qq.com/s?__biz=MzU1OTgyMDc3Mg==&mid=2247485442&idx=1&sn=086f54da49078a220584a8884ca9e344&chksm=fc103289cb67bb9f58bc2ca981b42f22b03575e2884a108052d7319e198c477f7955bf9c3826&token=1175739663&lang=zh_CN#rd)
