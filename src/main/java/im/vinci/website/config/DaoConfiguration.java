package im.vinci.website.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.DispatcherServlet;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 数据库配置
 * create by tim
 */
@Configuration
@EnableTransactionManagement
@MapperScan("im.vinci.website.persistence")
public class DaoConfiguration {

    @Autowired
    Environment env;

    @Bean(destroyMethod = "close")
    @Profile({UserProfile.INTG, UserProfile.QACI, UserProfile.PROD})
    public DataSource mysqlDataSource() throws SQLException {
        System.err.println("init mysql datasource");
        DruidDataSource dataSource = new DruidDataSource();
        String dbUrl = env.getProperty("jdbc.url");
        String dbUsername = env.getProperty("jdbc.username");
        String dbPassword = env.getProperty("jdbc.password");
        dataSource.setDriverClassName(env.getProperty("jdbc.driver", "com.mysql.jdbc.Driver"));
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        dataSource.setInitialSize(5);
        dataSource.setMaxActive(20);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQuery("select 1");
        dataSource.setMaxWait(5000);

        dataSource.setFilters("stat");
        return dataSource;
    }

        @Bean
        public SqlSessionFactory sqlSessionFactory() throws Exception {
            SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
            sqlSessionFactory.setDataSource(mysqlDataSource());
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactory.setMapperLocations(resolver
                    .getResources("classpath:/mapping/*.xml"));
            Resource resource = new ClassPathResource("database/mybatis-config.xml");
            sqlSessionFactory.setConfigLocation(resource);
            return sqlSessionFactory.getObject();
        }

        @Bean
        public JdbcTemplate jdbcTemplate() throws Exception {
            return new JdbcTemplate(mysqlDataSource());
    }

    @Bean
    public PlatformTransactionManager dataSourceTransactionManager() throws SQLException {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(mysqlDataSource());
        return dataSourceTransactionManager;
    }

    @Bean
    public TransactionTemplate transactionTemplate() throws SQLException {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(dataSourceTransactionManager());
        return transactionTemplate;
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    /**
     * Register dispatcherServlet programmatically
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean dispatcherServletRegistration() {

        ServletRegistrationBean registration = new ServletRegistrationBean(
                dispatcherServlet(), "/*");

        registration
                .setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);

        return registration;
    }

    /**
     * druid连接池的一些统计信息
     */
    @Bean
    public ServletRegistrationBean dispatcherServletDruidStatRegistration() {

        ServletRegistrationBean registration = new ServletRegistrationBean(
                new StatViewServlet(), "/druid/*");

        registration
                .setName("DruidStat");

        return registration;
    }
}



