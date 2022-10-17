package com.povobolapo.organizer.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

import static com.povobolapo.organizer.model.DictTaskStatus.NAME;

@Entity
@Table(name = NAME)
public class DictTaskStatus implements Serializable {
    public static final String NAME = "dict_task_status";
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "caption")
    private String caption;

    public DictTaskStatus() {
    }

    public DictTaskStatus(String name) {
        this.name = name;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
