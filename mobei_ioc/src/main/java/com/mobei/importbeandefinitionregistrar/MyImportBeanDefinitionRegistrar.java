package com.mobei.importbeandefinitionregistrar;

import com.mobei.dao.MyRegistrarDao;
import com.mobei.factorybean.MyFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 使用appicationContext.register和componentscan注册进容器的时候,
 * 我们都没法插手beandefinition的创建过程,而使用这种方式就可以
 * 重点:MyBatis中@MapperScan()动态往spring容器中添加dao接口的实现类的原理就是使用了这种方式
 *      @Import(MapperScannerRegistrar.class)
 * 		@Repeatable(MapperScans.class)
 * 		public @interface MapperScan{}
 *
 *		public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	//模拟mybatis动态往spring中添加代理对象
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		//1.得到beandefinition(mybatis是去扫描所有的接口)
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(MyRegistrarDao.class);
		GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) bdb.getBeanDefinition();
		genericBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(genericBeanDefinition.getBeanClassName());
		genericBeanDefinition.setBeanClass(MyFactoryBean.class);
		registry.registerBeanDefinition("myRegistrarDao", genericBeanDefinition);
	}
}
