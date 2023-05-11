package com.rrs.hotelapi.controllers;

import com.rrs.hotelapi.domain.Room;
import com.rrs.hotelapi.domain.Visitor;
import com.rrs.hotelapi.repository.RoomRepository;
import com.rrs.hotelapi.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

// Контроль запитів по URL
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

    // Сторінка привітання
    @GetMapping("")
    public String index() {
        return "Welcome to the Hotel API!";
    }


    // Додати новий номер
    @PostMapping("/rooms")
    public Room addRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }


    // Показати всі номери
    @GetMapping("/rooms")
    public List<Room> getRooms() {
        return roomRepository.findAllWithVisitors();
    }


    // Показати обрану кімнату
    @GetMapping("/rooms/{number}")
    public Room getRoom(@PathVariable String number) {
        return roomRepository.findByNumber(number);
    }


    // Оновити інформацію про номер
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


    // Видалити номер
    @DeleteMapping("/rooms/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        roomRepository.deleteById(roomId);
    }


    // Додати відвідувача до номеру
    @PostMapping("/rooms/{roomId}/visitors")
    public Room addVisitorToRoom(@PathVariable Long roomId, @RequestBody Visitor visitor) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            visitor.setRoom(room); // Встановлення зв'язку між відвідувачем і кімнатою
            room.getVisitors().add(visitor);
            room = roomRepository.save(room); // Зберегти зміни кімнати
            return room;
        }
        return null;
    }


    // Переселити відвідувача з номеру
    @PostMapping("/rooms/{roomId}/visitors/{visitorId}/move")
    public Room moveVisitorToRoom(@PathVariable Long roomId, @PathVariable Long visitorId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        Visitor visitor = visitorRepository.findById(visitorId).orElse(null);
        if (room != null && visitor != null) {
            room.getVisitors().remove(visitor);
            return roomRepository.save(room);
        }
        return null;
    }


    // Зберегти інформацію про відвідувача
    @PostMapping("/visitors")
    public Visitor saveVisitor(@RequestBody Visitor visitor) {
        return visitorRepository.save(visitor);
    }


    // Пошук відвідувача за прізвищем або номером паспорту
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


    // Оновлення інформації про відвідувача
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