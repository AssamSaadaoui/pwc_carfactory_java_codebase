package be.kdg.java2.carfactory_application.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final long id;

    public CustomUserDetails(long id, String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
