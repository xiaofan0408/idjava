package com.xiaofan0408.idjava.common.core;


import com.xiaofan0408.idjava.common.constant.Constant;
import com.xiaofan0408.idjava.mapper.IdJavaMapper;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xuzefan  2019/10/30 9:51
 */

public class MySQLIdGenerator {

    private String key;

    private AtomicLong cur;

    private long batch;

    private long batchMax;

    private IdJavaMapper idJavaMapper;

    public MySQLIdGenerator(IdJavaMapper idJavaMapper,String section) {
        this.idJavaMapper = idJavaMapper;
        setSection(section);
        this.batch = Constant.batchCount;
        this.cur = new AtomicLong(0);
        this.batchMax = this.cur.get();
    }


    public void setSection(String section) {
        this.key = section;
    }

    private long getIdFromMySQL() throws Exception {
        List<Long> ids = idJavaMapper.selectForUpdate(key);
        return ids.stream().findFirst().orElse(-1L);
    }

    public long current() {
        return this.cur.get();
    }

    public long next() throws Exception{
        long id = 0;
        boolean haveValue = false;
       List<Long> ids = idJavaMapper.selectForUpdate(this.key);

        if (batchMax < cur.get() +1 ){
            if (ids.size() > 0) {
                id = ids.stream().findFirst().get();
                haveValue = true;
            }
            //When the idgo table has no id key
            if (!haveValue) {
                return 0;
            }
            Integer update = idJavaMapper.updateId(this.key,this.batch);

            //batchMax is larger than cur BatchCount
            batchMax = id + Constant.batchCount;
            cur.set(id);
        }
        cur.getAndIncrement();
        return cur.get();
    }

    public void Init() throws Exception {
        cur.set(getIdFromMySQL());
        batchMax = cur.get();
    }

    public void Reset(long idOffset, boolean force) throws Exception {


        if (force) {
           idJavaMapper.dropTable(key);
           idJavaMapper.createTable(key);
        } else {
            idJavaMapper.createTableIfNotExists(key)    ;
            //check the idgo value if exist
            int rowCount = idJavaMapper.getRowCount(key);
            if (rowCount == 1) {
                cur.set(getIdFromMySQL());
                batchMax = cur.get();
            }
        }
        try {
            idJavaMapper.insertId(key,idOffset);
        }catch (Exception e){
            idJavaMapper.dropTable(key);
        }
        cur.set(idOffset);
        batchMax = cur.get();
    }

    public void DelKeyTable(String key) throws Exception {
        idJavaMapper.dropTable(key);
    }
}
