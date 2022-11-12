package com.povobolapo.organizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

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

    @Column(name = "file_extension")
    private String fileExtension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", referencedColumnName = "id")
    private ContentEntity content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_login", referencedColumnName = "login")
    private UserEntity owner;

    public ContentInfoEntity(String fileName, String fileExtension, ContentEntity content, UserEntity owner) {
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.content = content;
        this.owner = owner;
    }
}
