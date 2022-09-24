package com.povobolapo.organizer.dao;


import javax.persistence.*;
import java.io.Serializable;

import static com.povobolapo.organizer.dao.DictTaskStatus.NAME;

@Entity
@Table(name = NAME)
public class DictTaskStatus implements Serializable {
    public static final String NAME = "dict_task_status";
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "caption")
    private String caption;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
