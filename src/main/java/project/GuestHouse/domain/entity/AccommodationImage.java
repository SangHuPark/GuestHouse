package project.GuestHouse.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "accommodation_image")
public class AccommodationImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "origin_name")
    private String originName; // 이미지 파일의 본래 이름

    @Column(name = "stored_name")
    private String storedName; // S3에 저장된 이미지 파일 이름

    @Column(name = "access_url")
    private String accessUrl; // S3 URL

    @OneToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

}
