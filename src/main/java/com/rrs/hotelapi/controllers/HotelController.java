package com.rrs.hotelapi.controllers;

import com.rrs.hotelapi.domain.Room;
import com.rrs.hotelapi.domain.Visitor;
import com.rrs.hotelapi.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Контролює виконання дій при переході по URL
@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel")
class HotelController {
    private final HotelService hotelService;

    @GetMapping("")
    public String index() {
        return hotelService.index();
    }

    @PostMapping("/rooms")
    public Room addRoom(@RequestBody Room room) {
        return hotelService.addRoom(room);
    }

    @GetMapping("/rooms")
    public List<Room> getRooms() {
        return hotelService.getRooms();
    }

    @GetMapping("/rooms/{number}")
    public Room getRoom(@PathVariable String number) {
        return hotelService.getRoom(number);
    }

    @PutMapping("/rooms/{roomId}")
    public Room updateRoom(@PathVariable Long roomId, @RequestBody Room updatedRoom) {
        return hotelService.updateRoom(roomId, updatedRoom);
    }

    @DeleteMapping("/rooms/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        hotelService.deleteRoom(roomId);
    }

    @PostMapping("/rooms/{roomId}/visitors")
    public Room addVisitorToRoom(@PathVariable Long roomId, @RequestBody Visitor visitor) {
        return hotelService.addVisitorToRoom(roomId, visitor);
    }

    @DeleteMapping("/rooms/{roomId}/visitors/{visitorId}")
    public Room removeVisitorFromRoom(@PathVariable Long roomId, @PathVariable Long visitorId) {
        return hotelService.removeVisitorFromRoom(roomId, visitorId);
    }

    @GetMapping("/visitors")
    public List<Visitor> searchVisitors(@RequestParam(required = false) String lastName,
                                        @RequestParam(required = false) String passportNumber) {
        return hotelService.searchVisitors(lastName, passportNumber);
    }

    @PutMapping("/visitors/{visitorId}")
    public Visitor updateVisitor(@PathVariable Long visitorId, @RequestBody Visitor updatedVisitor) {
        return hotelService.updateVisitor(visitorId, updatedVisitor);
    }

    @PutMapping("/rooms/{oldRoomId}/visitors/{visitorId}/move/{newRoomId}")
    public Room moveVisitorToRoom(@PathVariable Long oldRoomId, @PathVariable Long visitorId,
                                  @PathVariable Long newRoomId) {
        return hotelService.moveVisitorToRoom(oldRoomId, visitorId, newRoomId);
    }

}