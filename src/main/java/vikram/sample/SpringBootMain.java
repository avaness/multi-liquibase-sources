package vikram.sample;

import java.sql.SQLException;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import vikram.sample.SpringBootMain.DatasourceConfig;

@SpringBootApplication
@EnableConfigurationProperties({DatasourceConfig.class})
public class SpringBootMain {

  @Autowired
  GenericApplicationContext springContext;

  public static void main(String[] args) throws LiquibaseException, SQLException {
    ConfigurableApplicationContext ctx = SpringApplication.run(SpringBootMain.class, args);
    applyMultiplePureLiquibases(ctx);

  }

  //TODO this is pure core liquibase appliance without any spring and beans wrapping
  //TODO pros:
  //TODO      - lightest way to use liquibase
  //TODO      - no needs of liquibase-spring integration
  //TODO      - easiest manual use of Liquibase().rollback(), report() and all other features
  //TODO
  //TODO contras:
  //TODO      - no support for generic spring resource like "file:", "classpath:", "url:"
  //TODO      - no support of "url:" at all, only ClassLoaderResourceAccessor() and FileResourceAccessor()
  //TODO      - no support for comprehensive classloaders for resources, like OSGI
  //TODO      - manually handle java.sql.connections
  private static void applyMultiplePureLiquibases(ConfigurableApplicationContext ctx)
      throws SQLException, LiquibaseException {

    DataSource masterDatasource = ctx.getBean(DataSource.class);
    applyPureLiquibase("liquibase/update-master.yaml", masterDatasource);

    DataSource existingClientDatasource = createDatasource(
        ctx.getBean("existing-client-datasource", DatasourceConfig.class));
    applyPureLiquibase("liquibase/update-existing-client.yaml", existingClientDatasource);

    DataSource newClientDatasource= createDatasource(
        ctx.getBean("new-client-datasource", DatasourceConfig.class));
    applyPureLiquibase("liquibase/setup-new-client.yaml", newClientDatasource);
  }

  private static void applyPureLiquibase(String changelog, DataSource clientDatasource)
      throws SQLException, LiquibaseException {
    //TODO connection is closed automatically after try with params block
    try(java.sql.Connection connection = clientDatasource.getConnection()) {

      Database database = DatabaseFactory
          .getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

      //TODO you should define is it "classpath:" or "file:" resource manually
      Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);

      liquibase.update(new Contexts(), new LabelExpression());
    }
  }

  //TODO this is 1st usecase.
  //TODO you can apply liquibase as beans manually in the code using GenericSpringContext
  //TODO
  //TODO pros:
  //TODO      - it allow to use all spring's resource loading features
  //TODO            like "file:", "classpath:" "url:" prefixes for example,
  //TODO            like native spring's work with eclipse's osgi model etc.
  //TODO      - you can use these beans for rolling schema back
  //TODO            and other liquibase features using maven (it's my hypothesis, still not sure)
  //TODO contras:
  //TODO      - you need GenericApplicationContext to be wired in some way
  //TODO      - some spring overhead on memory and startup time
  @Bean(name = "liquibase")
  public SpringLiquibase setupMultipleLiquibaseBeans(DataSource dataSource) {
    //TODO additional client beans, any amount you want
    springContext.registerBean(
        "setup-new-client",
        SpringLiquibase.class,
        () -> applyBeanLiquibase(
                  createDatasource(
                      springContext.getBean("new-client-datasource", DatasourceConfig.class)),
            "classpath:liquibase/setup-new-client.yaml")
    );
    springContext.registerBean(
        "update-existing-client",
        SpringLiquibase.class,
        ()-> applyBeanLiquibase(
                createDatasource(
                    springContext.getBean("existing-client-datasource", DatasourceConfig.class)),
            "classpath:liquibase/update-existing-client.yaml")
    );
    //TODO this is master database setup, you always need bean named "liquibase"
    //TODO properly defined for liquibase-spring integration
    return applyBeanLiquibase(dataSource, "classpath:liquibase/update-master.yaml");
  }

  private static SpringLiquibase applyBeanLiquibase(DataSource dataSource, String liquibaseModel) {
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

  private static BasicDataSource createDatasource(DatasourceConfig cfg) {
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
