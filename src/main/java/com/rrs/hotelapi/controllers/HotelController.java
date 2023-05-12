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
        return "Welcome to Hotel API";
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


    // Видалити відвідувача
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

    // Перегляд відвідувачів
    @GetMapping("/visitors")
    public List<Visitor> searchVisitors(@RequestParam(required = false) String lastName,
                                        @RequestParam(required = false) String passportNumber) {
        List<Visitor> visitors;
        if (lastName != null && passportNumber != null) {
            visitors = visitorRepository.findByLastNameAndPassportNumber(lastName, passportNumber);
        } else if (lastName != null) {
            visitors = visitorRepository.findByLastName(lastName);
        } else if (passportNumber != null) {
            visitors = Collections.singletonList(visitorRepository.findByPassportNumber(passportNumber));
        } else {
            visitors = visitorRepository.findAll();
        }

        for (Visitor visitor : visitors) {
            visitor.setRoomNumber(visitor.getRoomNumber());
        }

        return visitors;
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

    // Переселити відвідувача з однієї кімнати до іншої
    @PutMapping("/rooms/{oldRoomId}/visitors/{visitorId}/move/{newRoomId}")
    public Room moveVisitorToRoom(@PathVariable Long oldRoomId, @PathVariable Long visitorId, @PathVariable Long newRoomId) {
        Room oldRoom = roomRepository.findById(oldRoomId).orElse(null);
        Room newRoom = roomRepository.findById(newRoomId).orElse(null);
        if (oldRoom != null && newRoom != null) {
            Visitor visitor = visitorRepository.findById(visitorId).orElse(null);

            if (visitor != null && oldRoom.getVisitors().contains(visitor)) {
                if (newRoom.getVisitors().size() < newRoom.getCapacity()) {
                    // Remove visitor from old room
                    oldRoom.getVisitors().remove(visitor);
                    visitor.setRoom(null);
                    visitorRepository.save(visitor);

                    // Add visitor to new room
                    newRoom.getVisitors().add(visitor);
                    visitor.setRoom(newRoom);
                    visitorRepository.save(visitor);

                    // Update occupied status of rooms
                    oldRoom.setOccupied(!oldRoom.getVisitors().isEmpty());
                    roomRepository.save(oldRoom);

                    newRoom.setOccupied(true);
                    roomRepository.save(newRoom);

                    return newRoom;
                }
            }
        }
        return null;
    }
}