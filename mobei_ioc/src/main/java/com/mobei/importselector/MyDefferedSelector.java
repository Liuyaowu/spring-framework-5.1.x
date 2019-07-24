package com.mobei.importselector;

import com.mobei.dao.DeferredImportSelectorDao;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyDefferedSelector implements DeferredImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{DeferredImportSelectorDao.class.getName()};
	}
}
