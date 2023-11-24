package project.GuestHouse.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String originName; // 이미지 파일의 본래 이름

    private String storedName; // S3에 저장된 이미지 파일 이름

    private String accessUrl; // S3 URL

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Image(String originName) {
        this.originName = originName;
        this.storedName = "";
        this.accessUrl = "";
    }

}
