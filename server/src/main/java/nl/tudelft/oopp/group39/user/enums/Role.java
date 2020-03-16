package nl.tudelft.oopp.group39.user.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT, STAFF, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}