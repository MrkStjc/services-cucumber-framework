package spring;

import database.DatabasePropertiesUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static database.DatabaseConstants.*;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, DatabasePropertiesUtils databasePropertiesUtils) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("database.models");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(databasePropertiesUtils.prepareAdditionalFactoryConfiguration());
    return em;
  }

  // For enabling transactions on entity manager - e.g. we don't have to explicitly close db connection, it will be done automatically.
  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }

  @Bean
  public DataSource dataSource(DatabasePropertiesUtils databasePropertiesUtils) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    final Properties factoryPropertiesForDatabase =
            databasePropertiesUtils.getFactoryPropertiesForDatabase();
    dataSource.setDriverClassName(factoryPropertiesForDatabase.get(DRIVER).toString());
    dataSource.setUsername(factoryPropertiesForDatabase.get(USERNAME).toString());
    dataSource.setPassword(factoryPropertiesForDatabase.get(PASSWORD).toString());
    dataSource.setUrl(factoryPropertiesForDatabase.get(URL).toString());
    return dataSource;
  }

  // Just a thread safe proxy for entity manager.
  @Bean
  public SharedEntityManagerBean entityManager(EntityManagerFactory entityManagerFactory) {
    SharedEntityManagerBean sharedEntityManagerBean = new SharedEntityManagerBean();
    sharedEntityManagerBean.setEntityManagerFactory(entityManagerFactory);
    return sharedEntityManagerBean;
  }

  @Bean
  public DatabasePropertiesUtils dbProperties() {
    return new DatabasePropertiesUtils();
  }

}
