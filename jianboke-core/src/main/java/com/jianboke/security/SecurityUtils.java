package com.jianboke.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {
	public static String getCurrentUsername() {
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    String username = null;
	    if (authentication != null) {
	      if (authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
	        username = springSecurityUser.getUsername();
	      } else if (authentication.getPrincipal() instanceof String) {
	        username = (String) authentication.getPrincipal();
	      }
	    }
	    return username;
	}
}
