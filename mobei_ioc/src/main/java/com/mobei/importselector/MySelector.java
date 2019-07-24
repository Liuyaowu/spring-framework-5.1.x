package com.mobei.importselector;

import com.mobei.dao.ImportSelectorDao;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MySelector implements ImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{ImportSelectorDao.class.getName()};
	}
}
