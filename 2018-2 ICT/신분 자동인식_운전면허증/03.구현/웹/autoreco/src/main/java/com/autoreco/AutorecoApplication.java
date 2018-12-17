package com.autoreco;

import java.net.ServerSocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.autoreco.controller")
public class AutorecoApplication {
	
	static ServerSocket server=null;

	public static void main(String[] args) {
		SpringApplication.run(AutorecoApplication.class, args);
	}
}
