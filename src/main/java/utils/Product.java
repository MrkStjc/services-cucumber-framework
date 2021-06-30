package utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

  @JsonProperty("name")
  private String name;

  @JsonProperty("price")
  private String price;

  @JsonProperty("category")
  private String category;

  @JsonProperty("url")
  private String url;

  public String getName() {
    return name;
  }

  public String getPrice() {
    return price;
  }

  public String getCategory() {
    return category;
  }

  public String getUrl() {
    return url;
  }

}
