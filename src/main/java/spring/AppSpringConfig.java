package spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.models.requests.LoginRequest;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;
import utils.DataProvider;

import static java.lang.String.format;
import static java.lang.System.getProperty;

@Configuration
@ComponentScan(
  basePackages = {"framework", "utils", "database"})
@Import({DatabaseConfiguration.class})
public class AppSpringConfig {

  @Bean
  public Yaml yaml() {
    return new Yaml();
  }

  @Bean
  public ObjectMapper objectMapper() { return new ObjectMapper(); }

  @Bean
  public DataProvider<LoginRequest> newUserDataProvider() {
    String newUserDataPath = "testdata/newUserData.yml";
    return new DataProvider<>(newUserDataPath, LoginRequest.class);
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer
      .setLocations(
        new ClassPathResource(format("configuration/environments/%s.properties", getProperty("env"))),
        new ClassPathResource("configuration/runConfig.properties"));
    return configurer;
  }

}
