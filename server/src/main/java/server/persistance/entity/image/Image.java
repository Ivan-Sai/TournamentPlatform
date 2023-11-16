package server.persistance.entity.image;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import server.persistance.entity.BaseEntity;

@Entity
@Getter
@Setter
public class Image extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "contentType")
    private String contentType;
    @Lob()
    @Column(length = 100000)
    private byte[] bytes;
}
