package com.xiaofan0408.idjava.service;

import com.xiaofan0408.idjava.common.core.Result;
import reactor.core.publisher.Mono;

/**
 * @author xuzefan  2019/10/30 14:31
 */
public interface ApiService {
    Mono<Long> get(String key) throws Exception;

    Mono<Result> set(String idGenKey,Long idValue) throws Exception;

    Mono<Result> exists(String key) throws Exception;

    Mono<Result> del(String key) throws Exception;

    Mono<Result> select() throws Exception;


}
