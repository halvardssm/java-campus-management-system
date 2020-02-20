package nl.tudelft.oopp.demo.objects.facility.FacilityActions;

import nl.tudelft.oopp.demo.objects.facility.Exceptions.FacilityExistsException;
import nl.tudelft.oopp.demo.objects.facility.Facility;
import nl.tudelft.oopp.demo.objects.facility.Exceptions.FacilityNotFoundException;
import nl.tudelft.oopp.demo.objects.facility.FacilityRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.Exceptions.RoomFacilityNotFoundException;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacility;
import org.springframework.beans.factory.annotation.Autowired;

public class FacilityActions {


    @Autowired
    private FacilityRepository facilityRepository;

    public Facility readFacility(long id) throws FacilityNotFoundException {
        return facilityRepository.findById(id).orElseThrow(() -> new FacilityNotFoundException((int)id));
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
            return rf;
        }
        catch (FacilityNotFoundException e) {
            throw new FacilityNotFoundException(id);
        }
    }

}
