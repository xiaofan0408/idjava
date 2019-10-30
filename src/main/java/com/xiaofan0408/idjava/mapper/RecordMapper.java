package com.xiaofan0408.idjava.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author xuzefan  2019/10/30 14:30
 */

@Mapper
public interface RecordMapper {


//    CreateRecordTableSQLFormat = `
//    CREATE TABLE %s (
//            k VARCHAR(255) NOT NULL,
//    PRIMARY KEY (k)
//) ENGINE=Innodb DEFAULT CHARSET=utf8 `
//
//    //create key table if not exist
//    CreateRecordTableNTSQLFormat = `
//    CREATE TABLE IF NOT EXISTS %s (
//            k VARCHAR(255) NOT NULL,
//    PRIMARY KEY (k)
//) ENGINE=Innodb DEFAULT CHARSET=utf8 `
//
//    InsertKeySQLFormat  = "INSERT INTO %s (k) VALUES ('%s')"
//    SelectKeySQLFormat  = "SELECT k FROM %s WHERE k = '%s'"
//    SelectKeysSQLFormat = "SELECT k FROM %s"
//    DeleteKeySQLFormat  = "DELETE FROM %s WHERE k = '%s'"
//            )

    @Update(" CREATE TABLE ${tableName} (" +
            " k VARCHAR(255) NOT NULL," +
            "  PRIMARY KEY (k)" +
            ") ENGINE=Innodb DEFAULT CHARSET=utf8")
    Integer createRecordTable(@Param("tableName") String tableName);

    @Update(" CREATE TABLE IF NOT EXISTS ${tableName} (" +
            " k VARCHAR(255) NOT NULL," +
            "  PRIMARY KEY (k)" +
            ") ENGINE=Innodb DEFAULT CHARSET=utf8 ")
    Integer createRecordTableNT(@Param("tableName") String tableName);

    @Insert("INSERT INTO ${tableName} (k) VALUES ('${key}')")
    Integer insertKey(@Param("tableName") String tableName, @Param("key")String key);

    @Select("SELECT k FROM ${tableName} WHERE k = '${keyName}'")
    List<String> selectKey(@Param("tableName") String tableName, @Param("keyName")String keyName);

    @Select("SELECT k FROM ${tableName}")
    List<String> selectKeysS(@Param("tableName") String tableName);

    @Delete("DELETE FROM ${tableName} WHERE k = '${keyName}'")
    Integer deleteKey(@Param("tableName") String tableName, @Param("keyName") String keyName);
}
