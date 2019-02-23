package com.hehen.heweater.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hehen.heweater.bean.City;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * @author chenping
 * @date 2019/2/23 5:25 PM
 * @Description: 用于封装获取database
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private  String dataName = "hen_wedb";
    private  Context context;

    private static DatabaseHelper instance = null;
    //用户初始化工具类
    public static DatabaseHelper init(Context mContext, String dataName) {
        if (instance == null) {
            synchronized (DatabaseHelper.class){
                if(instance == null){
                    instance = new DatabaseHelper(mContext,dataName);
                }
            }
        }
        return instance;
    }

    public static DatabaseHelper  getInstance(){
        if(instance == null){
            throw  new IllegalArgumentException("you should can getInstance(Context context, String filename) when first time use !");
        }
        return instance;
    }

    private DatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
           TableUtils.createTable(connectionSource, City.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, City.class, true);

            onCreate(database);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}