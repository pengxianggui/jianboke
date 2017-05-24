package com.jianboke.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = true)
public class AppProperties {

  private final Work work = new Work();

  private final Async async = new Async();

  private final Security security = new Security();

  private final Map<String, Map<String, String>> datasource =
      new HashMap<String, Map<String, String>>();

  public Work getWork() {
    return work;
  }

  public Async getAsync() {
    return async;
  }

  public Security getSecurity() {
    return security;
  }

  public Map<String, Map<String, String>> getDatasource() {
    return datasource;
  }

  public static class Work {

    private String directory = "work";

    public String getDirectory() {
      return directory;
    }

    public void setDirectory(String directory) {
      this.directory = directory;
    }

  }

  public static class Async {

    private int corePoolSize = 2;

    private int maxPoolSize = 50;

    private int queueCapacity = 10000;

    public int getCorePoolSize() {
      return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
      this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
      return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
      this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
      return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
      this.queueCapacity = queueCapacity;
    }
  }

  public static class Security {

    @NotNull
    private String key;

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

  }

  public static class DataSource {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    public String getDriverClassName() {
      return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
      this.driverClassName = driverClassName;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

  }
}
