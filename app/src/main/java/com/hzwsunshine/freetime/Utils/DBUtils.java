package com.hzwsunshine.freetime.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import FreeTime.greenDao.CSDNCache;
import FreeTime.greenDao.CSDNCacheDao;
import FreeTime.greenDao.DaoMaster;
import FreeTime.greenDao.DaoSession;
import FreeTime.greenDao.ImageCache;
import FreeTime.greenDao.ImageCacheDao;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by 何志伟 on 2015/11/9.
 */
public class DBUtils {
    private static DaoSession daoSession;

    public static void setupDatabase(Context context) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        if(daoSession==null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "FreeTime-DB", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
    }

    /*******************************************************************************/
    public static ImageCacheDao getImageCacheDao() {
        return daoSession.getImageCacheDao();
    }

    public static CSDNCacheDao getCSDNCacheDao() {
        return daoSession.getCSDNCacheDao();
    }

    /*******************************************************************************/
    public static void addImageCache(String data, String totalNum, String urlType) {
        ImageCache note = new ImageCache(null, data, totalNum, urlType);
        getImageCacheDao().insert(note);
    }

    public static void addCSDNCache(String title, String date, String imgLink,
                                    String link, String content, String urlType) {
        CSDNCache note = new CSDNCache(null, title, date, imgLink, link, content, urlType);
        getCSDNCacheDao().insert(note);
    }

    /*******************************************************************************/
    public static List<ImageCache> queryImageCache(String urlType) {
        // Query 类代表了一个可以被重复执行的查询
        Query<ImageCache> query = getImageCacheDao().queryBuilder()
                .where(ImageCacheDao.Properties.UrlType.eq(urlType))
                .build();
        //查询结果以 List 返回
        List<ImageCache> data = query.list();
//        List data= getImageCacheDao().loadAll();
        return data;
    }

    public static List<CSDNCache> queryCSDNCache(String urlType) {
        Query<CSDNCache> query = getCSDNCacheDao().queryBuilder()
                .where(CSDNCacheDao.Properties.UrlType.eq(urlType))
                .orderAsc(CSDNCacheDao.Properties.Id)
//                .offset(0)
//                .limit(10)//每次取10条数据
                .build();
        List<CSDNCache> data = query.list();
        return data;
    }

    /*******************************************************************************/
    public static void clearImageCache(String urlType) {
        QueryBuilder<ImageCache> qb = getImageCacheDao().queryBuilder();
        DeleteQuery<ImageCache> bd = qb.where(ImageCacheDao.Properties.UrlType.eq(urlType)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }

    public static void clearCSDNCache(String urlType) {
        for (int i = 0; i < 10; i++) {
            QueryBuilder<CSDNCache> qb = getCSDNCacheDao().queryBuilder();
            DeleteQuery<CSDNCache> bd = qb.where(CSDNCacheDao.Properties.Date.eq(urlType)).buildDelete();
            bd.executeDeleteWithoutDetachingEntities();
        }
//        getCSDNCacheDao().deleteAll();
    }

    /*******************************************************************************/

    //根据日期去重
    private static final String DISTINCT = "SELECT DISTINCT DATE FROM CSDNCache";

    public static void DistinctCSDNByDate() {
        ArrayList<String> result = new ArrayList<>();
        Cursor c = daoSession.getDatabase().rawQuery(DISTINCT, null);
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        Log.i("xxx", result.toString());
    }

}


//    private static final String SQL_DISTINCT_ENAME = "SELECT DISTINCT "+
//                                EmpDao.Properties.EName.columnName+" FROM "+EmpDao.TABLENAME;
//
//    public static List<String> listEName(DaoSession session) {
//        ArrayList<String> result = new ArrayList<String>();
//        Cursor c = session.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
//        if (c.moveToFirst()) {
//            do {
//                result.add(c.getString(0));
//            } while (c.moveToNext());
//        }
//        c.close();
//        return result;
//    }
