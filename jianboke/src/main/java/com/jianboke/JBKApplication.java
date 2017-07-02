package com.jianboke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JBKApplication extends SpringBootServletInitializer
{
    public static void main( String[] args )
    {
    	SpringApplication.run(JBKApplication.class, args);
    }

    // 如果项目需要发布为war包，并部署外部容器
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JBKApplication.class);
    }
}
