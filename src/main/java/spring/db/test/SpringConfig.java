package spring.db.test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Properties;


/**
 * Created by joe321 on 2017/4/21.
 */
@EnableTransactionManagement
@ComponentScan("spring.db.test.data")
@Configuration
public class SpringConfig {

    private DataSource buildDataSource(Config dbConf) {
        String driverClassName  = dbConf.getString("driver");
        String jdbcUrl = dbConf.getString("url");
        String password = dbConf.getString("password");
        String userName = dbConf.getString("user");
        boolean autoCommit = dbConf.getBoolean("autoCommit");

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setAutoCommit(autoCommit);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setPassword(password);
        hikariConfig.setUsername(userName);
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "writeDS")
    @Primary
    public DataSource writeDataSourceBean(Config config) {
        Config dbConf = config.getConfig("db.write");
        return buildDataSource(dbConf);
    }

//    @Bean(name = "write2DS")
//    public DataSource write2DataSourceBean(Config config) {
//        Config dbConf = config.getConfig("db.write");
//        return buildDataSource(dbConf);
//    }

    @Bean(name = "readDS")
    public DataSource readDataSourceBean(Config config) {
        Config dbConf = config.getConfig("db.read");
        return buildDataSource(dbConf);
    }

    @Bean
    public PlatformTransactionManager getPlatformTransactionManager(@Qualifier("writeDS") DataSource writeDataSource) {
        return new DataSourceTransactionManager(writeDataSource);
    }

    @Bean
    public Config parsecConfig() {
        return ConfigFactory.load("dev.conf");
    }

//    @Bean
//    public Host getHost() {
//        return new Host();
//    }
}
