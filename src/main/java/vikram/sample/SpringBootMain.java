package vikram.sample;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableAutoConfiguration()
public class SpringBootMain {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootMain.class, args);
  }

  @Bean(name = "liquibase")
  public SpringLiquibase primaryLiquibase(DataSource dataSource) {
    SpringLiquibase springLiquibase = new SpringLiquibase();
    springLiquibase.setDataSource(dataSource);
    springLiquibase.setChangeLog("classpath:liquibase/changelog.yaml");
    springLiquibase.setShouldRun(true); //TODO
    return springLiquibase;

  }


}
