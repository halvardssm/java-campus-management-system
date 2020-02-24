package nl.tudelft.oopp.demo.objects.facility.Repositories;

import nl.tudelft.oopp.demo.objects.facility.Entities.Facility;
import nl.tudelft.oopp.demo.objects.room.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//This repository gets all data from the rooms table, and also queries to it
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("SELECT MAX(u.id) FROM Facility u")
    int getMaxId();

    //Returns an array of all facility ids
    @Query("SELECT id FROM Facility")
    int[] getAllFacilityIds();

    //Returns the facility that matches the inputted id param
    @Query("SELECT u FROM Facility u WHERE u.id = :id")
    Facility getFacilityById(@Param("id") long id);

}
