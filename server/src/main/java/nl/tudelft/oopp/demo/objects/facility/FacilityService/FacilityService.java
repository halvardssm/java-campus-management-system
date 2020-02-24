package nl.tudelft.oopp.demo.objects.facility.FacilityService;

import nl.tudelft.oopp.demo.objects.facility.Exceptions.FacilityExistsException;
import nl.tudelft.oopp.demo.objects.facility.Entities.Facility;
import nl.tudelft.oopp.demo.objects.facility.Exceptions.FacilityNotFoundException;
import nl.tudelft.oopp.demo.objects.facility.Repositories.FacilityRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityService.RoomFacilityService;
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
        return facilityRepository.findById(id).orElseThrow(() -> new FacilityNotFoundException((int)id));
    }

    public List<Facility> listFacilities() {
        return facilityRepository.findAll();
    }

    public Facility createFacility(Facility newFacility) {
        try {
            Facility Facility = readFacility((int)newFacility.getId());
            throw new FacilityExistsException((int)newFacility.getId());
        }
        catch (FacilityNotFoundException e){
            facilityRepository.save(newFacility);
            return newFacility;

        }
    }

    public void addFacility(String description) {
        int newId = facilityRepository.findAll().size() > 0 ? facilityRepository.getMaxId() + 1 : 0;
        Facility n = new Facility(newId,description);
        createFacility(n);
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
            Facility rf = readFacility((long) id);
            facilityRepository.delete(readFacility((long) id));
            rfService.deleteRoomFacilities(id, "facility");
            return rf;
        }
        catch (FacilityNotFoundException e) {
            throw new FacilityNotFoundException(id);
        }
    }

}
