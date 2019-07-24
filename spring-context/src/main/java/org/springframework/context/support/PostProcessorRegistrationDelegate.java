/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;

import java.util.*;

/**
 * Delegate for AbstractApplicationContext's post-processor handling.
 *
 * @author Juergen Hoeller
 * @since 4.0
 */
final class PostProcessorRegistrationDelegate {

	private PostProcessorRegistrationDelegate() {
	}


	/**
	 * 执行所有注册到容器中的BeanFactoryPostProcessors(自己手动添加的和spring默认创建的):
	 * 	在spring的BeanFactory初始化过程中去做一些事情
	 * 其中手动添加的BeanFactoryPostProcessor在工厂一个专门存储这个类型的集合中,
	 * 它对应的beandefinition并没有在工厂的beandefinitionmap集合中
	 *
	 * 1、先调用spring自己提供的ConfigurationClassPostProcessor类中的postProcessBeanDefinitionRegistry方法
	 * 		将初始化中提供的默认类的bean和register注册进来的bean加载到beanfactory中,然后处理@ComponentScan注解、
	 * 		再处理@Import注解,然后再处理自定义的BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法
	 * 		然后再处理此时扫描到的所有的BeanFactoryPostProcessor(接口或子接口实现的)的postProcessBeanFactory方法
	 * 2、上面执行BeanFactoryPostProcessor的postProcessBeanFactory方法时会去扫描配置类中是否加了@Configuration注解,
	 * 		@Bean注解的方法,此时扫描出来的bean就需要继续上面的步骤
	 *
	 * @param beanFactory:DefaultListableBeanFactory
	 * @param beanFactoryPostProcessors:自定义的BeanFactoryPostProcessor:自己写的并且没有交给spring去管理的(没有加@Component注解的)
	 */
	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// Invoke BeanDefinitionRegistryPostProcessors first, if any.
		//如果有BeanDefinitionRegistryPostProcessors则需要先处理,那么我们需要拿到beanfactory的beanDefinitionMap,
		//循环去查找是否有类型相匹配的类,如果找到了存储到这个set中
		Set<String> processedBeans = new HashSet<>();

		//必定成立:beanFactory实现了BeanDefinitionRegistry接口
		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

			//这个集合放的是我们自己写的实现了BeanFactoryPostProcessor接口的对象:
			// 比方说这里我们定义的MyBeanFactoryPostProcessor(没有交给spring容器管理才会出现在这里)
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();

			//这个集合放的是我们自己写的实现了BeanDefinitionRegistryPostProcessor接口的对象:
			// 扩展了一个方法(在beandefinition将要被加载的时候执行)
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
					//自定义的BeanDefinitionRegistryPostProcessor(手动注册进来的)的扩展方法在这里执行,
					// 如果动态添加了beandefinition,那么这里执行完后bdMap中就有了这个类的bd
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					registryProcessors.add(registryProcessor);
				}
				else {
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
			//BeanDefinitionRegistryPostProcessor继承自BeanFactoryPostProcessor
			//这个集合放的是spring内部自己实现了BeanDefinitionRegistryPostProcessor接口的对象:比方说ConfigurationClassPostProcessor
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
			//从beanFactory的beanDefinitionMap中拿出属于BeanDefinitionRegistryPostProcessor类型的类的字符串放到一个字符串数组中
			//这里其实就是ConfigurationClassPostProcessor
			//黑盒,详细代码可以忽略
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);

			/**
			 * 上面的分析可知这个地方可以得到一个BeanFactoryPostProcessor(ConfigurationClassPostProcessor)
			 * 为什么要在最开始注册这个呢:
			 * 	因为spring的工厂需要对bean进行解析、扫描等等功能,这些功能都不是由beanfactory自己完成的(由ConfigurationClassPostProcessor处理),
			 * 	这些功能都是需要在spring工厂初始化完成之前执行,必须在工厂最开始或工厂初始化之中,总之不能在初始化之后,因为那个时候已经不需要使用工厂了.
			 * 	所以这里spring在一开始就注册了BeanFactoryPostProcessor用来插手springfactory实例化过程.
			 * 	ConfigurationClassPostProcessor:具体作用可以参考源码
			 */
			for (String ppName : postProcessorNames) {//spring提供的才在beandefinition中,自己添加的在另外一个list集合中
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			//排序(实现了PriorityOrdered的类需要排序)不重要,而且currentRegistryProcessors中只有一个数据
			sortPostProcessors(currentRegistryProcessors, beanFactory);

			//合并list:将我们自己写的实现了BeanDefinitionRegistryPostProcessor接口的类和spring自己提供的实现了这个接口的类合并
			registryProcessors.addAll(currentRegistryProcessors);

			/**
			 * currentRegistryProcessors:ConfigurationClassPostProcessor
			 * registry:DefaultListableBeanFactory
			 * 1、先执行currentRegistryProcessors(spring自己创建的ConfigurationClassPostProcessor):
			 * 	先执行扩展方法(扩展方法在父类方法之前执行),在扩展方法里面处理了@Component和@Import等非常重要的注解
			 */
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);

			//执行完所有的BeanDefinitionRegistryPostProcessor
			//执行完后后面要复用,需要把内容清空
			currentRegistryProcessors.clear();

			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
			//2、排序
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			currentRegistryProcessors.clear();

			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
			//处理自定义的BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}

			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
			//执行所有BeanDefinitionRegistryPostProcessor
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);

			//执行自定义的BeanFactoryPostProcessor
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// Invoke factory processors registered with the context instance.
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		//上面的BeanFactoryPostProcessor的postProcessBeanFactory方法执行完后配置类可能存在@Bean方法的注解,
		// 这些注解注入的bean还需要继续上面的步骤去再处理
		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>();
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, invoke all other BeanFactoryPostProcessors.
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		beanFactory.clearMetadataCache();
	}

	/**
	 * 注册BeanPostProcessor
	 * @param beanFactory
	 * @param applicationContext
	 */
	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
		//从beanDefinitionMap中得到所有的BeanPostProcessor
		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// Register BeanPostProcessorChecker that logs an info message when
		// a bean is created during BeanPostProcessor instantiation, i.e. when
		// a bean is not eligible for getting processed by all BeanPostProcessors.
		/**
		 * 注册BeanPostProcessorChecker,这个类用来处理当一个bean没有资格被所有BeanPostProcessors处理时记录一条消息,例如:
		 * 某个方法需要传递参数并且标记了@Required注解(已过时),如果没有传递值就会报错,如果移除掉该后置处理器就不会报错了.
		 * 测试参考下面的:internalPostProcessors.remove(2)(具体是1还是2可以debug看BeanPostProcessorChecker在集合中的索引);
		 */
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// Separate between BeanPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		//区分PriorityOrdered、Ordered和其它接口的BeanPostProcessors
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				priorityOrderedPostProcessors.add(pp);
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					internalPostProcessors.add(pp);
				}
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, register the BeanPostProcessors that implement PriorityOrdered.
		//TODO 测试postProcessors功能
		// internalPostProcessors.remove(2);
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		// Next, register the BeanPostProcessors that implement Ordered.
		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>();
		for (String ppName : orderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			orderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// Now, register all regular BeanPostProcessors.
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// Finally, re-register all internal BeanPostProcessors.
		//		TODO 测试postProcessors功能
		// internalPostProcessors.remove(2);
		sortPostProcessors(internalPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// Re-register post-processor for detecting inner beans as ApplicationListeners,
		// moving it to the end of the processor chain (for picking up proxies etc).
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {
			comparatorToUse = OrderComparator.INSTANCE;
		}
		postProcessors.sort(comparatorToUse);
	}

	/**
	 * 执行实现了BeanDefinitionRegistryPostProcessor接口对象的扩展方法:postProcessBeanDefinitionRegistry(registry):
	 * 相当于所有实现了BeanDefinitionRegistryPostProcessor接口的扩展方法提前执行
	 * Invoke the given BeanDefinitionRegistryPostProcessor beans.
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}

	/**
	 * 执行实现了BeanFactoryPostProcessor接口对象的扩展方法:postProcessBeanDefinitionRegistry(registry):
	 * 相当于执行所有实现了BeanFactoryPostProcessor接口的通用方法(BeanDefinitionRegistryPostProcessor中的扩展方法提前执行了,通用方法也在这里执行)
	 * Invoke the given BeanFactoryPostProcessor beans.
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	/**
	 * Register the given BeanPostProcessor beans.
	 */
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

		for (BeanPostProcessor postProcessor : postProcessors) {
			beanFactory.addBeanPostProcessor(postProcessor);
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 *
	 * 当Spring的配置中的后置处理器还没有被注册就已经开始了bean的初始化,
	 * 便会打印出BeanPostProccessorChecker中设定的信息
	 *
	 */
	private static final class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(@Nullable String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
