package com.y2k.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MybatisConfig {
    
    @Value("${mybatis.mapper-locations}")
    private String location;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);

            Resource[] res = new PathMatchingResourcePatternResolver().getResources(location);
            sessionFactory.setMapperLocations(res);

            Properties mybatisProperties = new Properties();
            mybatisProperties.setProperty("mapUnderscoreToCamelCase", "true");
            sessionFactory.setConfigurationProperties(mybatisProperties);

            return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
