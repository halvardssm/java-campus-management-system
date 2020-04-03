package nl.tudelft.oopp.group39.user.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT, STAFF, ADMIN;

    /**
     * Gets the authority of the user.
     *
     * @return the authority of the user
     */
    @Override
    public String getAuthority() {
        return name();
    }
}