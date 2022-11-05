package com.povobolapo.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Column(name = "avatar", length = 128)
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
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
