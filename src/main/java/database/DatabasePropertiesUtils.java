package database;

import org.jasypt.properties.EncryptableProperties;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static database.DatabaseConstants.*;
import static java.lang.System.getProperty;
import static java.util.Objects.requireNonNull;
import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DatabasePropertiesUtils {

  @Value("${db.username}")
  private String dbUsername;

  @Value("${db.driver}")
  private String dbDriver;

  @Value("${db.show.sql}")
  private String dbShowSql;

  @Value("${db.format.sql}")
  private String dbFormatSql;

  @Value("${db.hibernate.dialect}")
  private String dbHibernateDialect;

  @Value("${db.url}")
  private String dbUrl;

  public Properties getFactoryPropertiesForDatabase() {
    Properties properties = new Properties();
    properties.setProperty(USERNAME, dbUsername);
    properties.setProperty(DRIVER, dbDriver);
    properties.setProperty(URL, dbUrl);
    BasicTextEncryptor encryptor = new BasicTextEncryptor();
    assertThat(null != getProperty("db.key")).as("Please use provided db.key in run configuration!").isTrue();
    encryptor.setPassword(getProperty("db.key"));
    Properties encryptableProperties = new EncryptableProperties(encryptor);
    String path = format(ENVIRONMENT_PROPERTIES_PATH, getProperty("env"));
    try (InputStream stream = getClass().getResourceAsStream(path)) {
      encryptableProperties.load(stream);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    requireNonNull(properties).setProperty(PASSWORD, requireNonNull(encryptableProperties).getProperty("db.password"));
    return properties;
  }

  public Properties prepareAdditionalFactoryConfiguration() {
    Properties properties = new Properties();
    properties.setProperty(SHOW_SQL, dbShowSql);
    properties.setProperty(FORMAT_SQL, dbFormatSql);
    properties.setProperty(DIALECT, dbHibernateDialect);
    return properties;
  }
}
