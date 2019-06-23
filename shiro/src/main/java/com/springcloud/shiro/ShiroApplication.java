package com.springcloud.shiro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ShiroApplication {


	protected static final Logger logger = LoggerFactory.getLogger(ShiroApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShiroApplication.class, args);

		logger.info("==================>" + "ShiroApplication start finished" + "<==================");
	}

}
