package com.rrs.hotelapi.repository;

import com.rrs.hotelapi.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Visitor findByPassportNumber(String passportNumber);

    List<Visitor> findByLastNameAndPassportNumber(String lastName, String passportNumber);

    List<Visitor> findByLastName(String lastName);
}