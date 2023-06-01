package com.rrs.hotelapi.repository;

import com.rrs.hotelapi.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
