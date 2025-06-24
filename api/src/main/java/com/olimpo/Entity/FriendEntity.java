package com.olimpo.Entity;

import java.security.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import com.olimpo.Enums.FriendRequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="friendship")
public class FriendEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @NotNull(message = "O id do usuário que enviou é obrigatório")
    @Column(name="sender_id")
    @Getter @Setter
    private int senderId;

    @NotNull(message = "O id do usuário que recebeu é obrigatório")
    @Column(name="receiver_id")
    @Getter @Setter
    private int receiverId;

    @NotNull(message = "O status é obrigatório")
    @Column(name="status")
    @Getter @Setter
    private FriendRequestStatus status;

    @CreationTimestamp(source=SourceType.DB)
    @Getter
    private LocalDateTime created_at;
}
