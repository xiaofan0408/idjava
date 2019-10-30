package com.xiaofan0408.idjava.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IdJavaMapper {

//    DropTableSQLFormat   = `DROP TABLE IF EXISTS %s`
//    InsertIdSQLFormat    = "INSERT INTO %s(id) VALUES(%d)"
//    SelectForUpdate      = "SELECT id FROM %s FOR UPDATE"
//    UpdateIdSQLFormat    = "UPDATE %s SET id = id + %d"
//    GetRowCountSQLFormat = "SELECT count(*) FROM %s"
//    GetKeySQLFormat      = "show tables like '%s'"

    @Update("CREATE TABLE ${tableName} (id bigint(20) unsigned NOT NULL auto_increment, PRIMARY KEY  (id)) ENGINE=Innodb DEFAULT CHARSET=utf8")
    Integer createTable(@Param("tableName") String tableName);

    @Update("CREATE TABLE IF NOT EXISTS  ${tableName}  (" +
            "    id bigint(20) unsigned NOT NULL auto_increment," +
            "    PRIMARY KEY  (id)" +
            ") ENGINE=Innodb DEFAULT CHARSET=utf8")
    Integer createTableIfNotExists(@Param("tableName") String tableName);

    @Update("DROP TABLE IF EXISTS ${tableName}")
    Integer dropTable(@Param("tableName") String tableName);

    @Insert("INSERT INTO ${tableName}(id) VALUES(${id})")
    Integer insertId(@Param("tableName") String tableName, @Param("id")Long id);

    @Select("SELECT id FROM ${tableName} FOR UPDATE")
    List<Long> selectForUpdate(@Param("tableName") String tableName);

    @Update("UPDATE ${tableName} SET id = id + ${num}")
    Integer updateId(@Param("tableName") String tableName,@Param("num") Long num);

    @Select("SELECT count(*) FROM ${tableName}")
    Integer  getRowCount(@Param("tableName") String tableName);

    @Select("show tables like '${key}'")
    List<String> getKey(@Param("key") String key);
}
