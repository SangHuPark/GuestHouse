package project.GuestHouse.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_nickname", nullable = false)
    private String userNickname;

    @Column(name = "birth", nullable = true)
    private LocalDate birth;

//    @Column(name = "profile_img", unique = true, nullable = true)
//    @Size(max = 300)
//    private String profileImg;

    @Column(name = "phone_num", unique = true, nullable = false)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private ProviderType provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "social_uuid")
    private Social social;

    @OneToOne(mappedBy = "user")
    private Image image;

    public void updatePassword(String password) {
        this.password = password;
    }

}
