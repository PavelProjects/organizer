package com.povobolapo.organizer.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "_task")
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

    public TaskEntity() {
    }

    public TaskEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public TaskEntity(DictTaskStatus dictTaskStatus, UserEntity author) {
        this.dictTaskStatus = dictTaskStatus;
        this.author = author;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DictTaskStatus getTaskStatus() {
        return dictTaskStatus;
    }

    public void setTaskStatus(DictTaskStatus dictTaskStatus) {
        this.dictTaskStatus = dictTaskStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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
