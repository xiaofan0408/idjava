package com.xiaofan0408.idjava.web;

import com.xiaofan0408.idjava.common.core.Result;
import com.xiaofan0408.idjava.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author xuzefan  2019/10/29 15:42
 */

@Api("id java")
@RestController
public class IdJavaController {

    @Autowired
    private ApiService apiService;


    @ApiOperation("get")
    @GetMapping("/get")
    public Mono<Result> get(@RequestParam("key") String key) throws Exception {
        return apiService.get(key);
    }

    @ApiOperation("set")
    @GetMapping("/set")
    public Mono<Result> set(@RequestParam("key") String key, @RequestParam("value") Long value) throws Exception {
        return apiService.set(key,value);
    }

}
