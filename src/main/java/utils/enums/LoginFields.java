package utils.enums;

public enum LoginFields {

  PASSWORD("password"),
  FIRST_NAME("firstName"),
  LAST_NAME("lastName"),
  USERNAME("username");

  private String name;

  LoginFields(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return getName();
  }

}
