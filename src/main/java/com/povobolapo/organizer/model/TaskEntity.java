package com.povobolapo.organizer.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.util.Streamable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "_task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TaskEntity implements Serializable, Streamable<UserEntity> {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "name")
    @ToString.Exclude
    private DictTaskStatus dictTaskStatus;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "deadline")
    private Date deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "login")
    @ToString.Exclude
    private UserEntity author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "_participants",
        joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_login", referencedColumnName = "login")}
    )
    @ToString.Exclude
    private Set<UserEntity> participants;

    public TaskEntity(DictTaskStatus status, UserEntity author, Set<UserEntity> participants) {
        this.dictTaskStatus = status;
        this.author = author;
        this.participants = participants;
    }

    @Override
    public Iterator<UserEntity> iterator() {
        return participants.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(creationDate.getTime(), that.creationDate.getTime())
                && Objects.equals(author.getLogin(), that.author.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
