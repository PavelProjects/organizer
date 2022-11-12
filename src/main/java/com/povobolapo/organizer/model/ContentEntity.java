package com.povobolapo.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "_content")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "hash_code")
    private int hashCode;

    public ContentEntity(Date creationDate, int hashCode) {
        this.creationDate = creationDate;
        this.hashCode = hashCode;
    }
}
