package com.rrs.hotelapi.services;

import com.rrs.hotelapi.domain.Room;
import com.rrs.hotelapi.domain.Visitor;
import com.rrs.hotelapi.repository.RoomRepository;
import com.rrs.hotelapi.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final RoomRepository roomRepository;
    private final VisitorRepository visitorRepository;

    public String index() {
        return "Welcome to Hotel API";
    }

    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getRooms() {
        return roomRepository.findAllWithVisitors();
    }

    public Room getRoom(String number) {
        return roomRepository.findByNumber(number);
    }

    public Room updateRoom(Long roomId, Room updatedRoom) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            room.setNumber(updatedRoom.getNumber());
            room.setCapacity(updatedRoom.getCapacity());
            room.setOccupied(updatedRoom.isOccupied());
            return roomRepository.save(room);
        }
        return null;
    }

    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    public Room addVisitorToRoom(Long roomId, Visitor visitor) {
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

    public Room removeVisitorFromRoom(Long roomId, Long visitorId) {
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

    public List<Visitor> searchVisitors(String lastName, String passportNumber) {
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

    public Visitor updateVisitor(Long visitorId, Visitor updatedVisitor) {
        Visitor visitor = visitorRepository.findById(visitorId).orElse(null);
        if (visitor != null) {
            visitor.setFirstName(updatedVisitor.getFirstName());
            visitor.setLastName(updatedVisitor.getLastName());
            visitor.setPassportNumber(updatedVisitor.getPassportNumber());
            return visitorRepository.save(visitor);
        }
        return null;
    }

    public Room moveVisitorToRoom(Long oldRoomId, Long visitorId, Long newRoomId) {
        Room oldRoom = roomRepository.findById(oldRoomId).orElse(null);
        Room newRoom = roomRepository.findById(newRoomId).orElse(null);
        if (oldRoom != null && newRoom != null) {
            Visitor visitor = visitorRepository.findById(visitorId).orElse(null);

            if (visitor != null && oldRoom.getVisitors().contains(visitor)) {
                if (newRoom.getVisitors().size() < newRoom.getCapacity()) {
                    oldRoom.getVisitors().remove(visitor);
                    visitor.setRoom(null);
                    visitorRepository.save(visitor);

                    newRoom.getVisitors().add(visitor);
                    visitor.setRoom(newRoom);
                    visitorRepository.save(visitor);

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

