package framework.steps;

import framework.models.requests.LoginRequest;
import io.cucumber.java.ParameterType;
import org.springframework.beans.factory.annotation.Autowired;
import utils.DataProvider;

public class ParameterTypes {

  @Autowired
  DataProvider<LoginRequest> newUserDataProvider;

  @ParameterType(".*")
  public LoginRequest convertToUserData(String userDataName) {
    return newUserDataProvider.getData(userDataName);
  }

}
