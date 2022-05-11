package com.backend.reditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.backend.reditclone.configuration.SwaggerConfiguration;



@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class SpringReditCloneApp {

	public static void main(String[] args)
	{
		SpringApplication.run(SpringReditCloneApp.class, args);
	}
}
