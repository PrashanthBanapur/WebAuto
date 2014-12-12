package org.seltest.driver;

import java.lang.reflect.Field;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class PageElementLocator implements ElementLocatorFactory{

	private final SearchContext context;

	public PageElementLocator(SearchContext context) {
		this.context = context;
	}

	@Override
	public ElementLocator createLocator(Field field) {
		return new DefaultElementLocator(context, field);
	}

}
