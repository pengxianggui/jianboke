package com.jianboke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JBKApplication 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(JBKApplication.class, args);
    }
}
