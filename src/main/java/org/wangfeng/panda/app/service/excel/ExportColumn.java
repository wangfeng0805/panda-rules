package org.wangfeng.panda.app.service.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportColumn {

    //列名
    String value() default "";

    String decryptMethod() default "";
}
