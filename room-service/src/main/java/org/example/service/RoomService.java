package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.RoomDTO;
import org.example.dto.RoomRequest;
import org.example.exceptionHandling.RoomNotFoundException;
import org.example.model.Room;
import org.example.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomDTO createRoom(RoomRequest req) {
        Room r = Room.builder()
                .name(req.getName())
                .location(req.getLocation())
                .capacity(req.getCapacity())
                .equipment(req.getEquipment())
                .build();
        return toDto(roomRepository.save(r));
    }

    public RoomDTO getRoom(Long id) {
        Room r = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));
        return toDto(r);
    }

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream().map(this::toDto).toList();
    }

    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id))
            throw new RoomNotFoundException("Room not found: " + id);
        roomRepository.deleteById(id);
    }

    private RoomDTO toDto(Room r) {
        return new RoomDTO(r.getId(), r.getName(), r.getLocation(), r.getCapacity(), r.getEquipment());
    }
}