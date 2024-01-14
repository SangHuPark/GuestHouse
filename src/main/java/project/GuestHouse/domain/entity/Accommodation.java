//package project.GuestHouse.domain.entity;
//
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.util.List;
//
//@Entity
//@Getter @Setter
//@Table(name = "accommodation")
//public class Accommodation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "accommodation_id")
//    private Long id;
//
//    @Column(name = "accommodation_name", nullable = true, length = 20)
//    private String accommodationName;
//
//    @Column(name = "description", nullable = true, length = 20)
//    private String description;
//
//    @Column(name = "address", nullable = true, length = 40)
//    private String address;
//
//    @Column(nullable = true)
//    private String x; // longitude 경도
//
//    @Column(nullable = true)
//    private String y; // latitude 위도
//
//    @Column(name = "bookmark")
//    private int bookmark;
//
//    @ManyToOne
//    @JoinColumn(name = "admin_id")
//    private Admin admin;
//
//    @OneToMany
//    @JoinColumn(name = "image_id")
//    private List<AccommodationImage> accommodationImage;
//
//}
