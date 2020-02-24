package nl.tudelft.oopp.demo.objects.facility.Repositories;

import nl.tudelft.oopp.demo.objects.facility.Entities.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
//This repository gets all data from the rooms table, and also queries to it
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("SELECT MAX(u.id) FROM Facility u")
    int getMaxId();


}
