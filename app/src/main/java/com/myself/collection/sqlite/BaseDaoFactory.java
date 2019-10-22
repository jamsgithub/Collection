package com.myself.collection.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * @author Jams
 * @date 2019/10/22 17:23
 * @des todo
 * @
 * @upAuthor
 * @upDate 2019/10/22 17:23
 * @upDes todo
 */
public class BaseDaoFactory {
    private static BaseDaoFactory instance;
    private final String sqLiteDataBasePath;
    private final SQLiteDatabase mSqLiteDatabase;

    private BaseDaoFactory() {
        sqLiteDataBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "myself.db";
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqLiteDataBasePath, null);

    }

    public static BaseDaoFactory getInstance() {
        if (instance == null) {
            synchronized (BaseDaoFactory.class) {
                if (instance == null) {
                    instance = new BaseDaoFactory();
                }
            }
        }
        return instance;
    }

    public <T> BaseDao<T> getBaseDao(Class<T> entity) {
        BaseDao baseDao = null;
        try {
            baseDao = BaseDao.class.newInstance();
            baseDao.init(entity, mSqLiteDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }

}
