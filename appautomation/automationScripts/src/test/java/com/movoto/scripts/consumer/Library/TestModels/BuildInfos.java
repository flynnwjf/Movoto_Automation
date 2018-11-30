package com.movoto.scripts.consumer.Library.TestModels;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BuildInfos {
	BuildInfo[] value() ;
}
