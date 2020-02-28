package nl.tudelft.oopp.group39.facility.service;

import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.exceptions.FacilityExistsException;
import nl.tudelft.oopp.group39.facility.exceptions.FacilityNotFoundException;
import nl.tudelft.oopp.group39.facility.repositories.FacilityRepository;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FacilityService {


    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private RoomRepository roomRepository;

    public Facility readFacility(long id) throws FacilityNotFoundException {
        return facilityRepository.findById(id).orElseThrow(() -> new FacilityNotFoundException((int) id));
    }

    public List<Facility> listFacilities() {
        return facilityRepository.findAll();
    }

    public Facility createFacility(Facility newFacility) {
        try {
            Facility facility = readFacility((int) newFacility.getId());
            throw new FacilityExistsException((int) facility.getId());
        } catch (FacilityNotFoundException e) {
            facilityRepository.save(newFacility);
            return newFacility;
        }
    }

    public Facility updateFacility(Facility newFacility, int id) throws FacilityNotFoundException {
        return facilityRepository.findById((long) id)
                .map(room -> {
                    room.setDescription(newFacility.getDescription());
                    return facilityRepository.save(room);
                }).orElseThrow(() -> new FacilityNotFoundException(id));
    }

    public Facility deleteFacility(int id) throws FacilityNotFoundException {
        try {
            Facility rf = readFacility(id);
            Room newRoom = new Room();
            List<Room> rooms = roomRepository.findAll();
            for (Room room : rooms) {
                Set<Facility> facilities = room.getFacilities();
                facilities.remove(rf);
                room.setFacilities(facilities);
            }
            facilityRepository.delete(readFacility(id));
            return rf;
        } catch (FacilityNotFoundException e) {
            throw new FacilityNotFoundException(id);
        }
    }

}
