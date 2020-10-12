package com.raksul.user;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("com.raksul.user")
public class AppConfig {
	

	@Autowired
	Environment environment;
	
	  
	  @Value("${DB_HOST}") String host;
	  
	  @Value("${DB_USER}") String user;
	  
	  @Value("${DB_PASS}") String password;
	  
	  @Value("${DB_NAME}") String dbname;
	  
	  @Value("${DB_PORT}") String dbPort;
	 

	@Bean
	DataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		String url = "jdbc:postgresql://" + host + ":" + dbPort + "/" + dbname;
		driverManagerDataSource.setUrl(url);
		driverManagerDataSource.setUsername(user);
		driverManagerDataSource.setPassword(password);
		driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
		return driverManagerDataSource;
	}
}
