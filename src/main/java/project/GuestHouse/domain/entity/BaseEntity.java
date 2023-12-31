package project.GuestHouse.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }

    @PrePersist
    public void onPrePersist(){
        this.createdAt = LocalDateTime.parse(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.updatedAt = LocalDateTime.parse(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}
