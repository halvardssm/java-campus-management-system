package nl.tudelft.oopp.role.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import nl.tudelft.oopp.role.enums.Roles;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = Role.TABLE_NAME)
public class Role implements GrantedAuthority {
    public static final String TABLE_NAME = "roles";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Roles role;

    public Role() {
    }

    public Role(String role) {
        this.role = Roles.valueOf(role);
    }

    public Role(Roles role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return this.role.name();
    }

    public void setAuthority(Roles role) {
        this.role = role;
    }

    @JsonIgnore
    public Roles getAuthorityAsRoles() {
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
