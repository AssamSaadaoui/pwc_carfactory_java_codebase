package be.kdg.java2.carfactory_application.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@PreAuthorize("hasRole('ROLE_ADMIN') " +
        "|| hasRole('ROLE_MANAGER')" +
        "|| (hasRole('ROLE_USER') && #currentUserName == principal.username)")
public @interface isAuthenticatedAsAdminOrManagerOrUserIsUserId {
}
