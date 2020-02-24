package nl.tudelft.oopp.group39.facility.FacilityService;

import nl.tudelft.oopp.group39.facility.Entities.Facility;
import nl.tudelft.oopp.group39.facility.Exceptions.FacilityExistsException;
import nl.tudelft.oopp.group39.facility.Exceptions.FacilityNotFoundException;
import nl.tudelft.oopp.group39.facility.Repositories.FacilityRepository;
import nl.tudelft.oopp.group39.roomFacility.RoomFacilityService.RoomFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityService {


    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private RoomFacilityService rfService;

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
            facilityRepository.delete(readFacility(id));
            rfService.deleteRoomFacilities(id, "facility");
            return rf;
        } catch (FacilityNotFoundException e) {
            throw new FacilityNotFoundException(id);
        }
    }

}
