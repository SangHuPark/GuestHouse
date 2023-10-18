package project.GuestHouse.domain.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import project.GuestHouse.domain.dto.user.UserDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String userName;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "phone_num", unique = true, nullable = false)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private ProviderType provider = ProviderType.LOCAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType = UserType.NORMAL;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "social_uuid")
    private Social social;

    public UserDto toDto() {
        return UserDto.builder()
                .email(this.email)
                .nickname(this.nickname)
                .build();
    }

}
