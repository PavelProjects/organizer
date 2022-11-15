package com.povobolapo.organizer.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/***
 * WARNING!!!
 * ИСПОЛЬЗОВАТЬ ТОЛЬКО ДЛЯ АВТОРИЗАЦИИ!!!
 */

@Entity
@Table(name = "_user_credits")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreditsEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "login", length = 32, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "mail", length = 128, nullable = false)
    private String mail;

    @Column(name = "active")
    private boolean active;

    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "_user_roles",
            joinColumns = @JoinColumn(
                    name = "user_login", referencedColumnName = "login"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    // Используется при создании юзера
    public UserCreditsEntity(String login, String encodePassword, String mail) {
        this.login = login;
        this.password = encodePassword;
        this.mail = mail;
        this.creationDate = new Date();
        this.active = true;
    }
}
