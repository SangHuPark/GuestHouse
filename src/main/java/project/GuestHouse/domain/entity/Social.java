package project.GuestHouse.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "social")
public class Social {

    @Id
    @Column(name = "social_uuid")
    private String uuid;


    @Column(name = "identifier")
    private String identifier;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
