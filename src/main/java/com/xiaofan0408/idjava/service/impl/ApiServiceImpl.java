package com.xiaofan0408.idjava.service.impl;

import com.xiaofan0408.idjava.common.constant.Constant;
import com.xiaofan0408.idjava.common.core.MySQLIdGenerator;
import com.xiaofan0408.idjava.common.core.Result;
import com.xiaofan0408.idjava.common.core.ResultGenerator;
import com.xiaofan0408.idjava.mapper.IdJavaMapper;
import com.xiaofan0408.idjava.mapper.RecordMapper;
import com.xiaofan0408.idjava.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author xuzefan  2019/10/30 14:31
 */
@Service
public class ApiServiceImpl implements ApiService{

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private IdJavaMapper idJavaMapper;

    private Map<String,MySQLIdGenerator> idGeneratorMap;

    @PostConstruct
    private void init() {
        idGeneratorMap = new ConcurrentHashMap<>();
        recordMapper.createRecordTableNT(Constant.KeyRecordTableName);
        List<String> idGenKeyList = recordMapper.selectKeysS(Constant.KeyRecordTableName);
        idGenKeyList.stream().forEach((idGenKey) ->{
            if (idGeneratorMap.get(idGenKey) == null) {
                boolean isExist = isKeyExist(idGenKey);
                if (isExist) {
                    MySQLIdGenerator idGenerator = new MySQLIdGenerator(idJavaMapper, idGenKey);
                    idGeneratorMap.put(idGenKey,idGenerator);
                }
            }
        });
    }

    @Override
    public Mono<Result> get(String key) throws Exception {
        return Mono.create(new Consumer<MonoSink<Result>>() {
            @Override
            public void accept(MonoSink<Result> resultMonoSink) {
                try {
                    if (StringUtils.isEmpty(key)) {
                        resultMonoSink.success(ResultGenerator.genFailResult("invalid key"));
                    }
                    MySQLIdGenerator idGenerator = idGeneratorMap.get(key);
                    if (idGenerator == null) {
                        resultMonoSink.success(ResultGenerator.genSuccessResult());
                    }
                    Long id = idGenerator.next();
                    resultMonoSink.success(ResultGenerator.genSuccessResult(id.toString()));
                }catch (Exception e){
                    resultMonoSink.error(e);
                }
            }
        }).publishOn(Schedulers.elastic());


    }

    @Override
    public Mono<Result> set(String idGenKey,Long idValue) throws Exception {
       return Mono.create(new Consumer<MonoSink<Result>>() {
           @Override
           public void accept(MonoSink<Result> resultMonoSink) {
               try {
                   if (StringUtils.isEmpty(idGenKey)) {
                       resultMonoSink.success(ResultGenerator.genFailResult("invalid key"));
                   }
                   MySQLIdGenerator idGenerator = idGeneratorMap.get(idGenKey);
                   if (idGenerator == null) {
                       idGenerator = new MySQLIdGenerator(idJavaMapper,idGenKey);
                       idGeneratorMap.put(idGenKey,idGenerator);
                   }
                   setKey(idGenKey);
                   idGenerator.Reset(idValue,false);
                   resultMonoSink.success(ResultGenerator.genSuccessResult("ok"));
               }catch (Exception e){
                   resultMonoSink.error(e);
               }
           }
       }).publishOn(Schedulers.elastic());
    }

    @Override
    public Mono<Result> exists(String key) throws Exception {
        return Mono.create(new Consumer<MonoSink<Result>>() {
            @Override
            public void accept(MonoSink<Result> resultMonoSink) {
                try {
                    if (StringUtils.isEmpty(key)) {
                        resultMonoSink.success(ResultGenerator.genFailResult("invalid key"));
                    }
                    MySQLIdGenerator idGenerator = idGeneratorMap.get(key);
                    if (idGenerator == null) {
                        resultMonoSink.success(ResultGenerator.genSuccessResult(false));
                    } else{
                        resultMonoSink.success(ResultGenerator.genSuccessResult(true));
                    }
                }catch (Exception e){
                    resultMonoSink.error(e);
                }
            }
        }).publishOn(Schedulers.elastic());
    }

    @Override
    public Mono<Result> del(String key) throws Exception {
        return Mono.create(new Consumer<MonoSink<Result>>() {
            @Override
            public void accept(MonoSink<Result> resultMonoSink) {
                try {
                    if (StringUtils.isEmpty(key)) {
                        resultMonoSink.success(ResultGenerator.genFailResult("invalid key"));
                    }
                    MySQLIdGenerator idGenerator =  idGeneratorMap.remove(key);
                    idGenerator.DelKeyTable(key);
                    delKey(key);
                    resultMonoSink.success(ResultGenerator.genSuccessResult(true));
                }catch (Exception e){
                    resultMonoSink.error(e);
                }
            }
        }).publishOn(Schedulers.elastic());
    }

    @Override
    public Mono<Result> select() throws Exception {
        return null;
    }


    private boolean isKeyExist(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        List<String> tableNames = idJavaMapper.getKey(key);
        if (tableNames.size() > 0 ) {
            return true;
        } else {
            return false;
        }
    }

    private String getKey(String key)  {
       List<String> keys =recordMapper.selectKey(Constant.KeyRecordTableName,key);
       if (keys.size() > 0) {
           return keys.stream().findFirst().orElse("");
       } else {
           return "";
       }
    }

    private void setKey(String key){
        if (StringUtils.isEmpty(key)) {
             return;
        }
       String temp = getKey(key);
        if (StringUtils.isEmpty(temp)) {
            recordMapper.insertKey(Constant.KeyRecordTableName,key);
        } else {
            return;
        }
    }

    private void  delKey(String key)  {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        String temp = getKey(key);
        if (StringUtils.isEmpty(temp)) {
           return;
        } else {
            recordMapper.deleteKey(Constant.KeyRecordTableName,key);
        }
    }
}
