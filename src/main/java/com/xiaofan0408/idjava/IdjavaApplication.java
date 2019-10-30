package com.xiaofan0408.idjava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@MapperScan("com.xiaofan0408.idjava.mapper")
@EnableSwagger2WebFlux
@SpringBootApplication
public class IdjavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdjavaApplication.class, args);
	}

}
