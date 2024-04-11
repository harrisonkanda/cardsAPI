package com.logicea.logiceacardsproject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.logicea.logiceacardsproject.enums.CardStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards", uniqueConstraints = { @UniqueConstraint(columnNames = {"name"})})
public class CardModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "card_id", columnDefinition = "BINARY(16)")
    private UUID cardId;
    @NotNull
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "color")
    private String color;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private CardStatus status = CardStatus.TODO; // default status, if no status is provided
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    @Column(name = "date_created")
    private Date dateCreated;

    @JsonManagedReference
    @JoinColumn(name = "owner", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private UserModel owner;
}
