package FreeTime.greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import FreeTime.greenDao.CSDNCache;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CSDNCACHE".
*/
public class CSDNCacheDao extends AbstractDao<CSDNCache, Long> {

    public static final String TABLENAME = "CSDNCACHE";

    /**
     * Properties of entity CSDNCache.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Date = new Property(2, String.class, "date", false, "DATE");
        public final static Property ImgLink = new Property(3, String.class, "imgLink", false, "IMG_LINK");
        public final static Property Link = new Property(4, String.class, "link", false, "LINK");
        public final static Property Content = new Property(5, String.class, "content", false, "CONTENT");
        public final static Property UrlType = new Property(6, String.class, "urlType", false, "URL_TYPE");
    };


    public CSDNCacheDao(DaoConfig config) {
        super(config);
    }
    
    public CSDNCacheDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CSDNCACHE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"DATE\" TEXT," + // 2: date
                "\"IMG_LINK\" TEXT," + // 3: imgLink
                "\"LINK\" TEXT," + // 4: link
                "\"CONTENT\" TEXT," + // 5: content
                "\"URL_TYPE\" TEXT);"); // 6: urlType
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CSDNCACHE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CSDNCache entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
 
        String imgLink = entity.getImgLink();
        if (imgLink != null) {
            stmt.bindString(4, imgLink);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(5, link);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
 
        String urlType = entity.getUrlType();
        if (urlType != null) {
            stmt.bindString(7, urlType);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CSDNCache readEntity(Cursor cursor, int offset) {
        CSDNCache entity = new CSDNCache( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // date
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // imgLink
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // link
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // content
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // urlType
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CSDNCache entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImgLink(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLink(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setContent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUrlType(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CSDNCache entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CSDNCache entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}