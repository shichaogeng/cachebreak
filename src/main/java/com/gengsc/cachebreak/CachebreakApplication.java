package com.gengsc.cachebreak;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.gengsc.cachebreak.mapper")
public class CachebreakApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachebreakApplication.class, args);
	}
}
