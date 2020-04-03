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


    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private RoomRepository roomRepository;

    public Facility readFacility(Long id) throws FacilityNotFoundException {
        return facilityRepository.findById(id)
            .orElseThrow(() -> new FacilityNotFoundException(id));
    }

    public List<Facility> listFacilities() {
        return facilityRepository.findAll();
    }

    /**
     * Creates a new facility.
     * @param newFacility the facility containing the new facility attributes.
     */
    public Facility createFacility(Facility newFacility) {
        return facilityRepository.save(newFacility);
    }

    /**
     * Updates an existing facility or throws a FacilityNotFoundException
     * if the facility that is to be updated isn't found.
     * @param id the id of the facility.
     * @param newFacility the facility containing the new updated facility attributes.
     */
    public Facility updateFacility(Facility newFacility, Long id) throws FacilityNotFoundException {
        return facilityRepository.findById(id)
            .map(facility -> facilityRepository.save(newFacility))
            .orElseThrow(() -> new FacilityNotFoundException(id));
    }

    /**
     * Deletes an existing facility or throws a FacilityNotFoundException
     * if the facility that is to be deleted isn't found.
     * @param id the id of the facility.
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
