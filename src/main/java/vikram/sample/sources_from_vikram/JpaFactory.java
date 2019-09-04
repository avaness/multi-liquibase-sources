/*
package vikram.sample.sources_from_vikram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.grc.jframework.jpa.configuration.config.JpaConfig;
import com.grc.jframework.jpa.data.annotation.PackagesToScan;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "jMainEntityManagerFactoryForShared",
    transactionManagerRef = "jMainTransactionManager"
)
@ComponentScan({"com.grc.jframework.jpa.dao.impl"})
public class JpaFactory implements BeanFactoryPostProcessor{
	
	 public static final String MAIN_ENTITY_MANAGER_BEAN = "jMainEntityManagerFactoryForShared";
	    public static final String MAIN_TRANSACTION_MANAGER_BEAN = "jMainTransactionManager";
	    public static final String MAIN_TRANSACTION_TEMPLATE_BEAN = "jMainTransactionTemplate";
	    private final List<String> packagesToScan = new ArrayList<>(Collections.singletonList("com.grc.jframework.jpa.data.jpa"));

	    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	        String[] configurations = beanFactory.getBeanNamesForAnnotation(Configuration.class);
	        String[] var3 = configurations;
	        int var4 = configurations.length;

	        for(int var5 = 0; var5 < var4; ++var5) {
	            String configuration = var3[var5];

	            Class proxy;
	            try {
	                proxy = Class.forName(beanFactory.getBeanDefinition(configuration).getBeanClassName());
	            } catch (ClassNotFoundException var10) {
	                throw new AssertionError("No bean definition class", var10);
	            }

	            Class configurationClass = proxy.getSuperclass();
	            PackagesToScan annotation = (PackagesToScan)configurationClass.getDeclaredAnnotation(PackagesToScan.class);
	            if (annotation != null) {
	                CollectionUtils.addAll(this.packagesToScan, annotation.value());
	            }
	        }

	    }

	    @Bean(
	        name = {"jMainEntityManagerFactoryForShared"}
	    )
	    @DependsOn({"jLiquibase"})
	    public LocalContainerEntityManagerFactoryBean entityManagerFactory(Environment env, @Qualifier("jDataSource") DataSource dataSource) {
	        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	        JpaConfig config = new JpaConfig(env);
	        factory.setDataSource(dataSource);
	        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
	        factory.setPackagesToScan((String[])this.packagesToScan.toArray(new String[1]));
	        factory.setJpaProperties(config.hibernateProperties);
	        factory.setValidationMode(ValidationMode.AUTO);
	        factory.afterPropertiesSet();
	        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
	        return factory;
	    }

	    @Bean(
	        name = {"jMainTransactionManager"}
	    )
	    @Primary
	    public PlatformTransactionManager transactionManager(@Qualifier("jMainEntityManagerFactoryForShared") EntityManagerFactory entityManagerFactory) {
	        JpaTransactionManager transactionManager = new JpaTransactionManager();
	        transactionManager.setEntityManagerFactory(entityManagerFactory);
	        return transactionManager;
	    }

	    @Bean(
	        name = {"jMainTransactionTemplate"}
	    )
	    public TransactionTemplate transactionTemplate(@Qualifier("jMainTransactionManager") PlatformTransactionManager transactionManager) {
	        return new TransactionTemplate(transactionManager);
	    }

}
*/
