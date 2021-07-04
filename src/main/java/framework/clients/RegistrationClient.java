package framework.clients;

import framework.BaseClient;
import framework.models.requests.LoginRequest;
import framework.models.responses.RegisterResponse;
import framework.models.responses.ResponseWrapper;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class RegistrationClient extends BaseClient {

  private static final String BY_USERNAME = "/users/%s";
  private static final String DELETE_ALL_USERS = "/users/deleteAllUsers";
  private static final String LOGIN = "/users/login";
  private static final String REGISTER = "/users/register";

  public ResponseWrapper<RegisterResponse> findUserByUsername(String username) {
    restClient.get(baseUrl + format(BY_USERNAME, username));
    return getWrappedResponseObject(RegisterResponse.class);
  }

  public void deleteUserByUsername(String username) {
    restClient.delete(baseUrl + format(BY_USERNAME, username));
  }

  public void deleteAllUsers() {
    restClient.delete(baseUrl + DELETE_ALL_USERS);
  }

  public void login(LoginRequest request) {
    restClient.post(baseUrl + LOGIN, request);
  }

  public ResponseWrapper<RegisterResponse> register(LoginRequest request) {
    restClient.put(baseUrl + REGISTER, request);
    return getWrappedResponseObject(RegisterResponse.class);
  }

}
