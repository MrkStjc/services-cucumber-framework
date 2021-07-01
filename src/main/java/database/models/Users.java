package database.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class Users implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "firstname", nullable = false)
  private String firstName;

  @Column(name = "lastname", nullable = false)
  private String lastName;

  @Column(name = "password_encrypted", nullable = false)
  private String passwordEncrypted;

  @Column(name = "user_email", nullable = false)
  private String userEmail;

  @Column(name = "user_fname")
  private String userFname;

  @Column(name = "user_lname", nullable = false)
  private String userLname;

  @Column(name = "user_mobile", nullable = false)
  private String userMobile;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "created_at", nullable = false)
  private String createdAt;

  public String getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPasswordEncrypted() {
    return passwordEncrypted;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public String getUserFname() {
    return userFname;
  }

  public String getUserLname() {
    return userLname;
  }

  public String getUserMobile() {
    return userMobile;
  }

  public String getUsername() {
    return username;
  }

  public String getCreatedAt() { return createdAt; }

}
