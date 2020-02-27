package nl.tudelft.oopp.group39.role.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.role.enums.Roles;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = Role.TABLE_NAME)
public class Role implements GrantedAuthority {
    public static final String TABLE_NAME = "roles";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;

    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }

    public Role(Roles role) {
        this.role = role.name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    @Transient
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role role1 = (Role) o;
        return getId().equals(role1.getId())
            && getAuthority().equals(role1.getAuthority());
    }
}
