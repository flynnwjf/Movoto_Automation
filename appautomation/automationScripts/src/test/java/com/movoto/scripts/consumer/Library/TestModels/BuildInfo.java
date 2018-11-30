package com.movoto.scripts.consumer.Library.TestModels;


import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Repeatable(BuildInfos.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface BuildInfo {
	public String PageName() default "";
	public String XPATH() default "";
	public String CssSelector() default "";
    public String ValueFrom() default "";

}

