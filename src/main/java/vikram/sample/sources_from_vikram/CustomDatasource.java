/*
package com.grc.riskanalysis.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.grc.common.Encryptor;
import com.grc.riskanalysis.facade.DbConnectionFacade;
import com.grc.riskanalysis.facade.ReportingUnitFacade;
import com.grc.riskanalysis.vo.admin.DbConnectionVO;

import liquibase.integration.spring.SpringLiquibase;

public class CustomDatasource implements DataSource {

	private static final Logger logger = Logger.getLogger(CustomDatasource.class.getName());

	private final Map<Long, BasicDataSource> datasources = new HashMap<>();

	private Long defaultDBId;

	@Autowired
	private DbConnectionFacade dbFacade;
	
	@Autowired
	private ReportingUnitFacade ruFacade;

	public CustomDatasource() {

	}
	
	
	@Bean(name="liquibase2")
	public SpringLiquibase configureNewClient( ) throws SQLException {
		//final List<DbConnectionVO> dbs = dbFacade.findAll();
		//for(DbConnectionVO)
		BasicDataSource ds = getDS();
	    return applyLiquibase(ds);
	}
	 
	private SpringLiquibase applyLiquibase(DataSource dataSource) {
	    SpringLiquibase springLiquibase = new SpringLiquibase();
	    springLiquibase.setDataSource(dataSource);
	    springLiquibase.setChangeLog("classpath:liquibase/changelog-ru.yaml");
	    springLiquibase.setShouldRun(true);
	    return springLiquibase;
	}
	
		
	private BasicDataSource getDS() throws SQLException {

		Long tenantId = TenetContext.getTenantId();
		synchronized (datasources) {
			if (datasources.size() == 0) {
				final List<DbConnectionVO> dbs = dbFacade.findAll();
				defaultDBId = dbs.get(0).getId();
				for (final DbConnectionVO db : dbs) {
					addDS(db);
				}
			}
			else if (tenantId != null && !datasources.containsKey(tenantId)) {
				final DbConnectionVO conn = dbFacade.findOne(tenantId);
				if (conn != null) {
					BasicDataSource ds=addDS(conn);
					applyLiquibase(ds);
				}
			}
		}
		tenantId = tenantId == null ? defaultDBId : tenantId;
		final BasicDataSource basicDataSource = datasources.get(tenantId);
		if (basicDataSource == null) {
			throw new SQLException("Data srouce with id: " + tenantId + " not found!");
		}
		return basicDataSource;
	}

	private BasicDataSource addDS(final DbConnectionVO db) {
		final BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(db.getDriverClassName());
		final String autRconnect = "?useSSL=false";
		String url = db.getUrl();
		if (!url.endsWith(autRconnect)) {
			url += autRconnect;
		}
		ds.setUrl(url);
		ds.setUsername(db.getUserName());
		
		// Decrypt, encrypted password to plain text so that the connection should successful.
		String password = db.getPassword();
		if(password.length() == 32){
			try {
				password = Encryptor.getIntance().decrypt(password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ds.setPassword(password);
		ds.setInitialSize(10);
		ds.setMaxTotal(100);
		ds.setMaxWaitMillis(20000);
		ds.setMinIdle(5);
		ds.setMaxIdle(20);
		ds.setTimeBetweenEvictionRunsMillis(300000);
		ds.setMinEvictableIdleTimeMillis(3600000L);
		ds.setRemoveAbandonedOnMaintenance(true);
		ds.setRemoveAbandonedTimeout(2700);
		ds.setTestWhileIdle(true);
		ds.setTestOnBorrow(true);
		ds.setTestOnReturn(true);
		ds.setRemoveAbandonedOnBorrow(true);
		ds.setValidationQuery("SELECT 1");
		ds.setLogExpiredConnections(false);
		//ds.setDefaultQueryTimeout(2700);
		//ds.setConnectionProperties("socketTimeout=4000000");
		//ds.setAccessToUnderlyingConnectionAllowed(true);
		//ds.setDefaultQueryTimeout(2400000);
		//ds.setRemoveAbandonedTimeout(900);
		datasources.put(db.getId(), ds);
		return ds;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return getDS().getLogWriter();
	}

	@Override
	public void setLogWriter(final PrintWriter out) throws SQLException {
		getDS().setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(final int seconds) throws SQLException {
		getDS().setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return getDS().getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return logger;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		if (iface == CustomDatasource.class) {
			return (T) this;
		}
		if (iface == BasicDataSource.class) {
			return (T) getDS();
		}
		if (iface.isAssignableFrom(CustomDatasource.class)) {
			return (T) this;
		}
		throw new SQLException("Exception in CustomDatasource.unwrap");
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {

		if (iface == CustomDatasource.class) {
			return true;
		}
		if (iface == BasicDataSource.class) {
			return true;
		}
		if (iface.isAssignableFrom(CustomDatasource.class)) {
			return true;
		}
		throw new SQLException("Exception in CustomDatasource.unwrap");
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getDS().getConnection();
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		return getDS().getConnection(username, password);
	}

}
*/
