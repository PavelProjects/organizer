package com.povobolapo.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "login", length = 32, nullable = false, unique = true)
    private String login;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "avatar", length = 8)
    private String avatar;

    public UserEntity(String login, String name, String avatar) {
        this.login = login;
        this.name = name;
        this.avatar = avatar;
    }

    public UserEntity(String login, String name) {
        this.login = login;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return login.equals(that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
