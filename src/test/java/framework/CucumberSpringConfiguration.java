package framework;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import spring.AppSpringConfig;

@CucumberContextConfiguration
@SpringBootTest(classes = AppSpringConfig.class)
public class CucumberSpringConfiguration {
}
