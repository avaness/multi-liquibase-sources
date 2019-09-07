/*
package vikram.sample.sources_from_vikram;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@PropertySource({"classpath:database.properties"})
public class DatabaseFactory {
	
	 public static final String LIQUIBASE_BEAN = "jLiquibase";
	    public static final String DATASOURCE_BEAN = "jDataSource";
	   
	    @Autowired
	    private Environment env;

	    public DatabaseFactory() {
	    }

	    @Bean(
	        name = {"jDataSource"}
	    )
	    protected DataSource getDataSource() throws Exception {
	        String tomcatContext = this.env.getProperty("database.tomcat.context_name");
	        if (tomcatContext == null) {
	            BasicDataSource bds = new BasicDataSource();
	            bds.setDriverClassName(this.env.getRequiredProperty("database.driver"));
	            bds.setUrl(this.env.getRequiredProperty("database.url"));
	            bds.setUsername(this.env.getRequiredProperty("database.user"));
	            bds.setPassword(this.env.getRequiredProperty("database.password"));
	            bds.setMaxTotal((Integer)this.env.getProperty("database.pool.max_active", Integer.class, 20));
	            bds.setInitialSize((Integer)this.env.getProperty("database.pool.initial_size", Integer.class, 10));
	            bds.setMinIdle((Integer)this.env.getProperty("database.pool.min_idle", Integer.class, 0));
	            bds.setMaxIdle((Integer)this.env.getProperty("database.pool.max_idle", Integer.class, 10));
	            bds.setMaxWaitMillis((Long)this.env.getProperty("database.pool.max_wait", Long.class, 10L));
	            return bds;
	        } else {
	            return (DataSource)(new InitialContext()).lookup("java:comp/env/" + tomcatContext);
	        }
	    }

	    @Bean(
	        name = {"jLiquibase"}
	    )
	    protected SpringLiquibase springLiquibase(@Qualifier("jDataSource") DataSource dataSource) throws Exception {
	        SpringLiquibase springLiquibase = new SpringLiquibase();
	        springLiquibase.setChangeLog("classpath:liquibase/changelog-startup.yaml");
	        springLiquibase.setDataSource(dataSource);
	        springLiquibase.setShouldRun((Boolean)this.env.getProperty("database.liquibase.enabled", Boolean.class, Boolean.FALSE));
	        return springLiquibase;
	    }

}
*/
