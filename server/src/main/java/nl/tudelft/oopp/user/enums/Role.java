package nl.tudelft.oopp.user.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT, STAFF, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}