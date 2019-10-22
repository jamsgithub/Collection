package com.myself.collection.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myself.collection.sqlite.annotation.DBField;
import com.myself.collection.sqlite.annotation.DBTable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Jams
 * @date 2019/10/22 15:42
 * @des todo
 * @
 * @upAuthor
 * @upDate 2019/10/22 15:42
 * @upDes todo
 */
public class BaseDao<T> implements IBaseDao<T> {
    private SQLiteDatabase mDatabase;
    private Class<T> entityClass;
    private String tableName;
    private Map<String , Field> cacheMap;

    public synchronized boolean init(Class<T> entity , SQLiteDatabase sqLiteDatabase){
        this.mDatabase = sqLiteDatabase;
        this.entityClass = entity;
        if (!sqLiteDatabase.isOpen()){
            return false;
        }
        tableName = entity.getAnnotation(DBTable.class).value();
        if (!autoCreatedTable()){
            return false;
        }

        initCacheMap();
        return false;
    }

    /**
     * 拼接创建表语句，并执行
     * @return
     */
    private boolean autoCreatedTable() {
        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists " + tableName + " ( ");
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field  : fields) {
            Class type = field.getType();
            if (type == String.class){
                sb.append(field.getAnnotation(DBField.class).value() + " TEXT ,");
            }else if (type == Double.class){
                sb.append(field.getAnnotation(DBField.class).value() + " DOUBLE ,");
            }else if (type == Long.class){
                sb.append(field.getAnnotation(DBField.class).value() + " BIGINT ,");
            }else if (type == Integer.class){
                sb.append(field.getAnnotation(DBField.class).value() + " INTEGER ,");
            }else if (type == byte[].class){
                sb.append(field.getAnnotation(DBField.class).value() + " BLOB ,");
            }else {
                //不支持的类型
                continue;
            }

        }

        //去掉最后一个,
        if (sb.toString().charAt(sb.toString().length() -1 ) == ','){
            sb.deleteCharAt(sb.toString().length() -1);
        }
        sb.append(")");
        try {
            //执行建表语句
            this.mDatabase.execSQL(sb.toString());
        }catch (Exception e){
            return false;
        }

        return true;
    }

    /**
     * 将表中的字段跟创建的实体类中的属性映射起来
     */
    private void initCacheMap(){
        cacheMap = new HashMap<>();
        //映射关系
        //情况1：版本升级，最新版本，在某个表中删除一个字段，由于数据库版本没有更新成功，
        //导致插入这个对象时产生崩溃
        //情况2：其他开发者更改了表结构

        //查一次表(空表)
        String sql = "select * from " + tableName + " limit 1 , 0";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        Field[] fields = entityClass.getDeclaredFields();
        Field resultField = null;
        for (String columnName  : columnNames) {
            for (Field field  : fields) {
                if (columnName.equals(field.getAnnotation(DBField.class).value())){
                    resultField = field;
                    continue;
                }

            }
            cacheMap.put(columnName , resultField);
        }
        cursor.close();
    }

    @Override
    public long insert(T entityClass) {
        ContentValues contentValues = createValues(entityClass);
        mDatabase.insert(tableName , null , contentValues);
        return 0;
    }

    private ContentValues createValues(T entity){
        ContentValues contentValues = new ContentValues();
        Iterator<Map.Entry<String, Field>> iterator = cacheMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Field> fieldEntry = iterator.next();
            String key = fieldEntry.getKey();
            Field field = fieldEntry.getValue();
            field.setAccessible(true);
            try {
                //获取字段对应的值
                Object object = field.get(entity);
                Class type = field.getType();
                if (type == String.class){
                    String value = (String) object;
                    contentValues.put(key , value);
                }else if (type == Double.class){
                    Double value = (Double) object;
                    contentValues.put(key , value);
                }else if (type == Integer.class){
                    Integer value = (Integer) object;
                    contentValues.put(key , value);
                }else if (type == Long.class){
                    Long value = (Long) object;
                    contentValues.put(key , value);
                }else if (type == byte[].class){
                    byte[] value = (byte[]) object;
                    contentValues.put(key , value);
                }else {
                    continue;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }
}
