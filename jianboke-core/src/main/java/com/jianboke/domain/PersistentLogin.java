package com.jianboke.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogin implements Serializable {

  private static final long serialVersionUID = 7670138420369616203L;

  private static final int MAX_USER_AGENT_LEN = 255;

  @Id
  private String series;

  @JsonIgnore
  @NotNull
  @Column(name = "token")
  private String token;

  @Column(name = "last_used")
  private LocalDateTime lastUsed;

  // an IPV6 address max length is 39 characters
  @Size(min = 0, max = 39)
  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "user_agent")
  private String userAgent;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public String getSeries() {
    return series;
  }

  public void setSeries(String series) {
    this.series = series;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LocalDateTime getLastUsed() {
    return lastUsed;
  }

  public void setLastUsed(LocalDateTime lastUsed) {
    this.lastUsed = lastUsed;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    if (userAgent.length() >= MAX_USER_AGENT_LEN) {
      this.userAgent = userAgent.substring(0, MAX_USER_AGENT_LEN - 1);
    } else {
      this.userAgent = userAgent;
    }
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(series).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    PersistentLogin rhs = (PersistentLogin) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj)).append(series, rhs.series).isEquals();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
