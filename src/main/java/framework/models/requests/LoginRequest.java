package framework.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LoginRequest {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String firstName;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String lastName;

  private String password;

  private String username;

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
