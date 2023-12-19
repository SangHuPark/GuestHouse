//package project.GuestHouse.domain.entity;
//
//import lombok.*;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Getter
//@Setter
//@Table(name = "admin")
//public class Admin extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "admin_id")
//    private Long id;
//
//    @Column(name = "admin_email", nullable = false, unique = true, length = 45)
//    private String adminEmail;
//
//    @Column(name = "password", nullable = false)
//    private String password;
//
//    @Column(name = "admin_name", nullable = false, length = 20)
//    private String adminName;
//
//    @Column(name = "admin_nickname", nullable = false, length = 20)
//    private String adminNickname;
//
//    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
//    @JoinColumn(name = "accommodation_id")
//    private List<Accommodation> accommodation;
//
//}
