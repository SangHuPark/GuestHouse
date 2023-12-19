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
//    @Column(name = "introduce", nullable = true, length = 20)
//    private String introduce;
//
//    @Column(name = "address", nullable = true, length = 40)
//    private String address;
//
//    @Column(name = "longitude", nullable = true)
//    private BigDecimal longitude;
//
//    @Column(name = "latitude", nullable = true)
//    private BigDecimal latitude;
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
