package vikram.sample;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import vikram.sample.SpringBootMain.VikramCustomDatasourceConfig;

@SpringBootApplication
@EnableConfigurationProperties(VikramCustomDatasourceConfig.class)
public class SpringBootMain {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootMain.class, args);
  }

  @Bean(name = "liquibase")
  public SpringLiquibase primaryLiquibase(DataSource dataSource) {
    return applyLiquibase(dataSource);
  }

  @Bean(name="liquibase2")
  public SpringLiquibase configure2ndDatasource(VikramCustomDatasourceConfig cfg) {
    BasicDataSource ds = createDatasource(cfg);
    return applyLiquibase(ds);
  }

  private SpringLiquibase applyLiquibase(DataSource dataSource) {
    SpringLiquibase springLiquibase = new SpringLiquibase();
    springLiquibase.setDataSource(dataSource);
    springLiquibase.setChangeLog("classpath:liquibase/changelog.yaml");
    springLiquibase.setShouldRun(true); //TODO
    return springLiquibase;
  }

  @ConfigurationProperties(prefix = "vikram.custom.datasource2")
  public static class VikramCustomDatasourceConfig {
    private String url;
    private String driverClassName;
    private String username;
    private String password;

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getDriverClassName() {
      return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
      this.driverClassName = driverClassName;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }

  private BasicDataSource createDatasource(VikramCustomDatasourceConfig cfg) {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(cfg.getDriverClassName());
    ds.setUrl(cfg.getUrl());
    ds.setUsername(cfg.getUsername());
    ds.setPassword(cfg.getPassword());
    ds.setRemoveAbandonedOnMaintenance(true);
    ds.setRemoveAbandonedOnBorrow(true);
    ds.setValidationQuery("SELECT 1");
    ds.setLogExpiredConnections(false);
    return ds;
  }
}
