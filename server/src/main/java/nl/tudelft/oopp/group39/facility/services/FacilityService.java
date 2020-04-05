package nl.tudelft.oopp.group39.facility.services;

import java.util.List;
import java.util.Set;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.exceptions.FacilityNotFoundException;
import nl.tudelft.oopp.group39.facility.repositories.FacilityRepository;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {
    @Autowired private FacilityRepository facilityRepository;
    @Autowired private RoomRepository roomRepository;

    /**
     * Reads a facility.
     *
     * @param id the id of the facility that you want to get
     * @return the requested facility
     * @throws FacilityNotFoundException if the facility wasn't found
     */
    public Facility readFacility(Long id) throws FacilityNotFoundException {
        return facilityRepository.findById(id)
            .orElseThrow(() -> new FacilityNotFoundException(id));
    }

    /**
     * Lists all facilities.
     *
     * @return a list of facilities
     */
    public List<Facility> listFacilities() {
        return facilityRepository.findAll();
    }

    /**
     * Doc. TODO Sven
     */
    public Facility createFacility(Facility newFacility) {
        return facilityRepository.save(newFacility);
    }

    /**
     * Doc. TODO Sven
     */
    public Facility updateFacility(Facility newFacility, Long id) throws FacilityNotFoundException {
        return facilityRepository.findById(id)
            .map(facility -> facilityRepository.save(newFacility))
            .orElseThrow(() -> new FacilityNotFoundException(id));
    }

    /**
     * Doc. TODO Sven
     */
    public Facility deleteFacility(Long id) throws FacilityNotFoundException {
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
