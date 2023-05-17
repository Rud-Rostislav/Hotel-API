package com.rrs.hotelapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Поля для БД про відвідувачів
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String passportNumber;
    private LocalDateTime joinDate;
    private LocalDateTime leaveDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    @Transient
    private String roomNumber;

    public String getRoomNumber() {
        if (room != null) {
            return room.getNumber();
        }
        return null;
    }
}