package project.GuestHouse.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_name", length = 20)
    private String roomName;

    @Column(name = "room_people", length = 15)
    private String roomPeople;

    @Column(name = "original_price", length = 45)
    private int originalPrice;

    @Column(name = "discount_rate")
    private BigDecimal discountRate;
}
