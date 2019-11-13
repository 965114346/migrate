package com.zyy.migrate.annotation;

import java.lang.annotation.*;

/**
 * 字段映射
 * @author yangyang.zhang
 * @date 2019年11月11日16:26:16
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColumnMapping {

    String sourceColumn() default "";

    String targetColumn();

    String defaultInsertValue() default "";
}
