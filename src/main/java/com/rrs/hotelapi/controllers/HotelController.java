package com.rrs.hotelapi.controllers;

import com.rrs.hotelapi.domain.Room;
import com.rrs.hotelapi.domain.Visitor;
import com.rrs.hotelapi.repository.RoomRepository;
import com.rrs.hotelapi.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/hotel")
class HotelController {
    private final RoomRepository roomRepository;
    private final VisitorRepository visitorRepository;

    @Autowired
    public HotelController(RoomRepository roomRepository, VisitorRepository visitorRepository) {
        this.roomRepository = roomRepository;
        this.visitorRepository = visitorRepository;
    }

    // Привітання. Головна
    @GetMapping("")
    public String index() {
        return "НАШО ТИ СЮДИ ЗАЙШОВ?";
    }

    // Створення кімнати
    @PostMapping("/rooms")
    public Room addRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    // Перегляд кімнат
    @GetMapping("/rooms")
    public List<Room> getRooms() {
        return roomRepository.findAllWithVisitors();
    }

    // Перегляд кімнати
    @GetMapping("/rooms/{number}")
    public Room getRoom(@PathVariable String number) {
        return roomRepository.findByNumber(number);
    }

    // Оновити дані кімнати
    @PutMapping("/rooms/{roomId}")
    public Room updateRoom(@PathVariable Long roomId, @RequestBody Room updatedRoom) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            room.setNumber(updatedRoom.getNumber());
            room.setCapacity(updatedRoom.getCapacity());
            room.setOccupied(updatedRoom.isOccupied());
            return roomRepository.save(room);
        }
        return null;
    }

    // Видалити кімнату
    @DeleteMapping("/rooms/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        roomRepository.deleteById(roomId);
    }

    // Додати відвідувача до кімнати
    @PostMapping("/rooms/{roomId}/visitors")
    public Room addVisitorToRoom(@PathVariable Long roomId, @RequestBody Visitor visitor) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            if (room.getVisitors().size() >= room.getCapacity()) {
                // Кімната вже заповнена максимальною кількістю відвідувачів
                return null;
            }

            visitor.setRoom(room);
            visitorRepository.save(visitor);
            room.getVisitors().add(visitor);
            room.setOccupied(true);
            return roomRepository.save(room);
        }
        return null;
    }


    // Видалити відвідувача з кімнати
    @DeleteMapping("/rooms/{roomId}/visitors/{visitorId}")
    public Room removeVisitorFromRoom(@PathVariable Long roomId, @PathVariable Long visitorId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            Visitor visitor = visitorRepository.findById(visitorId).orElse(null);
            if (visitor != null) {
                visitor.setRoom(null);
                visitorRepository.delete(visitor);
                room.getVisitors().remove(visitor);
                return roomRepository.save(room);
            }
        }
        return null;
    }

    // Перемістити відвідувача з кімнати в іншу ДОРОБИТИ!!!!!!
    @PostMapping("/rooms/{roomId}/visitors/{visitorId}/move")
    public Room moveVisitorToRoom(@PathVariable Long roomId, @PathVariable Long visitorId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        Visitor visitor = visitorRepository.findById(visitorId).orElse(null);
        if (room != null && visitor != null) {
            room.getVisitors().remove(visitor);
            visitor.setRoom(null);
            visitorRepository.save(visitor);
            return roomRepository.save(room);
        }
        return null;
    }

    // Додати відвідувача ВИДАЛИТИ АБО ДОРОБИТИ!!!!!!
    @PostMapping("/visitors")
    public Visitor saveVisitor(@RequestBody Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    // Перегляд відвідувачів
    @GetMapping("/visitors")
    public List<Visitor> searchVisitors(@RequestParam(required = false) String lastName,
                                        @RequestParam(required = false) String passportNumber) {
        if (lastName != null && passportNumber != null) {
            return visitorRepository.findByLastNameAndPassportNumber(lastName, passportNumber);
        } else if (lastName != null) {
            return visitorRepository.findByLastName(lastName);
        } else if (passportNumber != null) {
            return Collections.singletonList(visitorRepository.findByPassportNumber(passportNumber));
        } else {
            return visitorRepository.findAll();
        }
    }

    // Оновити дані відвідувача
    @PutMapping("/visitors/{visitorId}")
    public Visitor updateVisitor(@PathVariable Long visitorId, @RequestBody Visitor updatedVisitor) {
        Visitor visitor = visitorRepository.findById(visitorId).orElse(null);
        if (visitor != null) {
            visitor.setFirstName(updatedVisitor.getFirstName());
            visitor.setLastName(updatedVisitor.getLastName());
            visitor.setPassportNumber(updatedVisitor.getPassportNumber());
            return visitorRepository.save(visitor);
        }
        return null;
    }
}