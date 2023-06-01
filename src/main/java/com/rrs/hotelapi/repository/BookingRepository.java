package com.rrs.hotelapi.repository;

import com.rrs.hotelapi.domain.Booking;
import com.rrs.hotelapi.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByVisitor(Visitor visitor);
}
