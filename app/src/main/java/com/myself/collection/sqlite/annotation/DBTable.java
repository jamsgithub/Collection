package com.myself.collection.sqlite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jams
 * @date 2019/10/22 15:26
 * @des 注解类:用于创建表名
 * @
 * @upAuthor
 * @upDate 2019/10/5:26
 * @upDes todo
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {
    String value();
}
