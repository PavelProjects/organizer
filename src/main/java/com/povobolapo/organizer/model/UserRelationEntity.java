package com.povobolapo.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "_user_relation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "creation_date")
    private Date creationDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_user_login", referencedColumnName = "login")
    private UserEntity firstUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_user_login", referencedColumnName = "login")
    private UserEntity secondUser;

    @Column(name = "relation_type")
    private String relationType;

    public UserRelationEntity(UserEntity secondUser, UserEntity firstUser, RelationType relationType) {
        this.secondUser = secondUser;
        this.firstUser = firstUser;
        this.relationType = relationType.name();
    }

    public enum RelationType {
        REQUEST,
        IGNORE,
        FRIEND
    }
}
