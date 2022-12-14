package com.povobolapo.organizer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "_user")
@Data
@ToString
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "creation_date")
    private Date creationDate = new Date();

    @Column(name = "login", length = 32, nullable = false, unique = true)
    private String login;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar", referencedColumnName = "id")
    private ContentInfoEntity avatar;

    public UserEntity(String login, String name, ContentInfoEntity avatar) {
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
