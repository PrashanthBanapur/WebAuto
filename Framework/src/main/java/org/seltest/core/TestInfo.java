package org.seltest.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestInfo {

	public enum Priority {
		LOW, MEDIUM, HIGH
	}

	Priority priority() default Priority.MEDIUM;

	String author();

	String lastModified();

	String version() default "1.0";

}
