package com.rrs.hotelapi.repository;

import com.rrs.hotelapi.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Додає поля із Room до БД
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByNumber(String number);

    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.visitors")
    List<Room> findAllWithVisitors();
}