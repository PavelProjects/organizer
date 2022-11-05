package com.povobolapo.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "_task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskEntity implements Serializable {
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
    private DictTaskStatus dictTaskStatus;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "deadline")
    private Date deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "login")
    private UserEntity author;

    public TaskEntity(DictTaskStatus status, UserEntity author) {
        this.dictTaskStatus = status;
        this.author = author;
    }

    @Override
    public String toString() {
        return String.format(
                "id: %s\nname: %s\n", id, name
        );
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
