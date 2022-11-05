package com.povobolapo.organizer.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

import static com.povobolapo.organizer.model.DictTaskStatus.NAME;

@Entity
@Table(name = NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}
