package com.povobolapo.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

// На эту сущность необходимо ссылаться из других сущностей
// Например из комментария, если туда прикрепили какой-то файл

@Entity
@Table(name = "_content_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentInfoEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", referencedColumnName = "id")
    private ContentEntity content;

    @Column(name = "owner")
    private String owner;

    public ContentInfoEntity(String fileName, ContentEntity content, String owner) {
        this.fileName = fileName;
        this.content = content;
        this.owner = owner;
    }
}
