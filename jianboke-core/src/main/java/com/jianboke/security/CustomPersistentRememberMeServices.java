package com.jianboke.security;


import com.jianboke.config.AppProperties;
import com.jianboke.domain.PersistentLogin;
import com.jianboke.repository.PersistentLoginRepository;
import com.jianboke.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class CustomPersistentRememberMeServices extends AbstractRememberMeServices {

  private final Logger log = LoggerFactory.getLogger(CustomPersistentRememberMeServices.class);

  // Token is valid for one month
  private static final int TOKEN_VALIDITY_DAYS = 31;

  private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;

  private static final int DEFAULT_SERIES_LENGTH = 16;

  private static final int DEFAULT_TOKEN_LENGTH = 16;

  private SecureRandom random;

  @Autowired
  private PersistentLoginRepository persistentLoginRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  public CustomPersistentRememberMeServices(AppProperties appConfig,
                                            UserDetailsService userDetailsService) {

    super(appConfig.getSecurity().getKey(), userDetailsService);
    random = new SecureRandom();
  }

  @Override
  @Transactional
  protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
      HttpServletResponse response) {

    PersistentLogin login = getPersistentLogin(cookieTokens, request, response);
    String username = login.getUser().getUsername();

    log.debug("Refreshing persistent login token for user '{}', series '{}'", username,
        login.getSeries());
    login.setLastUsed(LocalDateTime.now());
    login.setToken(generateTokenData());
    login.setIpAddress(request.getRemoteAddr());
    login.setUserAgent(request.getHeader("User-Agent"));
    try {
      persistentLoginRepository.saveAndFlush(login);
      addCookie(login, request, response);
    } catch (DataAccessException e) {
      log.error("Failed to update token: ", e);
      throw new RememberMeAuthenticationException("Autologin failed due to data access problem", e);
    }
    return getUserDetailsService().loadUserByUsername(username);
  }

  @Override
  protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication successfulAuthentication) {
    String username = successfulAuthentication.getName();

    log.debug("Creating new persistent login for user {}", username);
    PersistentLogin login = userRepository.findOneByUsername(username).map(u -> {
      PersistentLogin t = new PersistentLogin();
      t.setSeries(generateSeriesData());
      t.setUser(u);
      t.setToken(generateTokenData());
      t.setLastUsed(LocalDateTime.now());
      t.setIpAddress(request.getRemoteAddr());
      t.setUserAgent(request.getHeader("User-Agent"));
      return t;
    }).orElseThrow(
        () -> new UsernameNotFoundException("User " + username + " was not found in the database"));
    try {
      persistentLoginRepository.saveAndFlush(login);
      addCookie(login, request, response);
    } catch (DataAccessException e) {
      log.error("Failed to save persistent token ", e);
    }
  }

  @Override
  @Transactional
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String rememberMeCookie = extractRememberMeCookie(request);
    if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
      try {
        String[] cookieTokens = decodeCookie(rememberMeCookie);
        PersistentLogin persistentLogin = getPersistentLogin(cookieTokens, request, response);
        persistentLoginRepository.delete(persistentLogin);
      } catch (InvalidCookieException ice) {
        log.info("Invalid cookie, no persistent token could be deleted");
      } catch (RememberMeAuthenticationException rmae) {
        log.debug("No persistent token found, so no token could be deleted");
      }
    }
    super.logout(request, response, authentication);
  }

  /**
   * Validate the token and return it.
   */
  private PersistentLogin getPersistentLogin(String[] cookieTokens, HttpServletRequest request,
                                             HttpServletResponse response) {
    if (cookieTokens.length != 2) {
      throw new InvalidCookieException("Cookie token did not contain " + 2
          + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
    }

    final String presentedSeries = cookieTokens[0];
    final String presentedToken = cookieTokens[1];
    System.out.println(presentedSeries);
    PersistentLogin persistentLogin = persistentLoginRepository.findOne(presentedSeries);

    if (persistentLogin == null) {
      // No series match, so we can't authenticate using this cookie
      throw new RememberMeAuthenticationException(
          "No persistent token found for series id: " + presentedSeries);
    }

    // We have a match for this user/series combination
    log.info("presentedToken={} / tokenValue={}", presentedToken, persistentLogin.getToken());
    if (!presentedToken.equals(persistentLogin.getToken())) {
      // Token doesn't match series value. Delete this session and throw
      // an exception.
      persistentLoginRepository.delete(persistentLogin);
//      throw new CookieTheftException(
//          "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
      cancelCookie(request, response);
      throw new RememberMeAuthenticationException("Remember-me login has expired");
    }

    if (persistentLogin.getLastUsed().plusDays(TOKEN_VALIDITY_DAYS).isBefore(LocalDateTime.now())) {
      persistentLoginRepository.delete(persistentLogin);
      cancelCookie(request, response);
      throw new RememberMeAuthenticationException("Remember-me login has expired");
    }
    return persistentLogin;
  }

  private String generateSeriesData() {
    byte[] newSeries = new byte[DEFAULT_SERIES_LENGTH];
    random.nextBytes(newSeries);
    return new String(Base64.encode(newSeries));
  }

  private String generateTokenData() {
    byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
    random.nextBytes(newToken);
    return new String(Base64.encode(newToken));
  }

  private void addCookie(PersistentLogin persistentLogin, HttpServletRequest request,
      HttpServletResponse response) {
    setCookie(new String[] {persistentLogin.getSeries(), persistentLogin.getToken()},
        TOKEN_VALIDITY_SECONDS, request, response);
  }
}
