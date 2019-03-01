package com.ezuazu.mutant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ezuazu.mutant.writer.AsyncDatabaseWriter;

@SpringBootApplication
@ComponentScan("com.ezuazu")
public class MutantApplication {

	@Autowired
	public AsyncDatabaseWriter asyncWriter;
	
	public static void main(String[] args) {
		SpringApplication.run(MutantApplication.class, args);
	}

	@PostConstruct
	private void startPost() {
		asyncWriter.run();
	}
	
}
