package im.vinci.website.dbserver;

import javax.sql.DataSource;

import im.vinci.website.config.UserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@EnableTransactionManagement
//@MapperScan("im.vinci.website.persistence")
@Profile(UserProfile.UNIT_TEST)
public class EmbeddedDataSourceConfig {

    @Bean(destroyMethod="shutdown")
    public DataSource mysqlDataSource() {
        return new EmbeddedMysqlDatabaseBuilder().addSqlScript("/database_test.sql").build();
    }
}