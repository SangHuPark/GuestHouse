package project.GuestHouse.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "Social")
public class Social {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @OneToOne(mappedBy = "social")
    private User user;

    @Column(name = "user_uuid", unique = true, nullable = false)
    private String userUuid;

    @Column(name = "identifier")
    private String identifier;

}
