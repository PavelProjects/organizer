package com.povobolapo.organizer.dao;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dict_notify_type")
public class DictNotifyType implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "caption")
    private String caption;

    public DictNotifyType() {
    }

    public DictNotifyType(Integer id, String name, String caption) {
        this.id = id;
        this.name = name;
        this.caption = caption;
    }

    public DictNotifyType(String name, String caption) {
        this.name = name;
        this.caption = caption;
    }

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
