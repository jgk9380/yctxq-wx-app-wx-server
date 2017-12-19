package com.config.jpa.datasouce;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by jianggk on 2017/1/20.
 */
@Configuration
public class PrimaryDataSourceConfig {
/**
 * spring.datasource.primary.url=jdbc:oracle:thin:@130.34.22.3:1521:ora11g
 * spring.datasource.primary.username=commmanager
 * spring.datasource.primary.password=commmanager
 *
 **/
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}


//
//SpringBoot读取application.properties文件，通常有3种方式
//        1. @Value  例如：
//@Value("${spring.profiles.active}")
//private String profileActive;------相当于把properties文件中的spring.profiles.active注入到变量profileActive中
//        2. @ConfigurationProperties  例如：
//@Component
//@ConfigurationProperties(locations = "classpath:application.properties",prefix="test")
//public class TestProperties {
//    String url;
//    String key;
//}
//其他类中使用时，就可以直接注入该TestProperties 进行访问相关的值
//        3. 使用Enviroment   例如：
//private Enviroment env;
//        env.getProperty("test.url");
//        而env方式效率较低