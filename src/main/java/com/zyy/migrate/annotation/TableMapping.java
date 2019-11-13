package com.zyy.migrate.annotation;

import java.lang.annotation.*;

/**
 * 表映射
 * @author yangyang.zhang
 * @date 2019年11月11日16:26:16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableMapping {

    String sourceTable();

    String targetTable();
}
