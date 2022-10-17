package com.povobolapo.organizer.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dict_notify_type")
public class DictNotifyType implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "caption")
    private String caption;

    public DictNotifyType() {
    }

    public DictNotifyType(String id, String name, String caption) {
        this.id = id;
        this.name = name;
        this.caption = caption;
    }

    public DictNotifyType(String name, String caption) {
        this.name = name;
        this.caption = caption;
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
