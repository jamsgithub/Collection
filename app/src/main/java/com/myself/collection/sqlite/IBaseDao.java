package com.myself.collection.sqlite;

/**
 * @author Jams
 * @date 2019/10/22 15:25
 * @des todo
 * @
 * @upAuthor
 * @upDate 2019/10/22 15:25
 * @upDes todo
 */
public interface IBaseDao<T> {
    long insert(T entityClass);

}
