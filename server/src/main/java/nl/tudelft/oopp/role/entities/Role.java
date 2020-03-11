package nl.tudelft.oopp.role.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT, STAFF, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}