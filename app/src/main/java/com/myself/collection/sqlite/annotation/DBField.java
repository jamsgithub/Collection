package com.myself.collection.sqlite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jams
 * @date 2019/10/22 15:29
 * @des 用于实体类属性与数据库字段关联
 * @
 * @upAuthor
 * @upDate 2019/10/22 15:29
 * @upDes todo
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBField {
    String value();
}
