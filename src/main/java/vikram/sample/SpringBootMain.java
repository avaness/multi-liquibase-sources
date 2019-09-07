package vikram.sample;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import vikram.sample.SpringBootMain.DatasourceConfig;

@SpringBootApplication
@EnableConfigurationProperties({DatasourceConfig.class})
public class SpringBootMain {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootMain.class, args);
  }

  @Bean(name = "liquibase")
  public SpringLiquibase setupMaster(DataSource dataSource) {
    return applyLiquibase(dataSource, "classpath:liquibase/update-master.yaml");
  }

  @Bean(name="setup-new-client")
  @Autowired
  public SpringLiquibase configureNewClient(@Qualifier("new-client-datasource") DatasourceConfig cfg) {
    BasicDataSource ds = createDatasource(cfg);
    return applyLiquibase(ds, "classpath:liquibase/setup-new-client.yaml");
  }

  @Bean(name="update-existing-client")
  @Autowired
  public SpringLiquibase updateExistingClient(@Qualifier("existing-client-datasource") DatasourceConfig cfg) {
    BasicDataSource ds = createDatasource(cfg);
    return applyLiquibase(ds, "classpath:liquibase/update-existing-client.yaml");
  }

  private SpringLiquibase applyLiquibase(DataSource dataSource, String liquibaseModel) {
    SpringLiquibase springLiquibase = new SpringLiquibase();
    springLiquibase.setDataSource(dataSource);
    springLiquibase.setChangeLog(liquibaseModel);
    springLiquibase.setShouldRun(true);
    return springLiquibase;
  }

  @Bean(name="new-client-datasource")
  @ConfigurationProperties(prefix="vikram.custom.new.client")
  public DatasourceConfig newClientDatasource() {
    return new DatasourceConfig();
  }

  @Bean(name="existing-client-datasource")
  @ConfigurationProperties(prefix="vikram.custom.existing.client")
  public DatasourceConfig existingClientDatasource() {
    return new DatasourceConfig();
  }

  @ConfigurationProperties
  public static class DatasourceConfig {
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

  private BasicDataSource createDatasource(DatasourceConfig cfg) {
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
