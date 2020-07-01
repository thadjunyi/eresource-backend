package com.springboot.eresourceaccesssystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class EresourceAccessSystemApplication {

	public static void main(String[] args) throws InvalidKeyException,
			NoSuchAlgorithmException, IOException{
		SpringApplication.run(EresourceAccessSystemApplication.class, args);
	}

}
