/**
 * 
 */
package com.jianboke.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pengxg
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD}) //声明下面定义的注解所修饰的作用范围
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnComment {
	String value() default "";
}
