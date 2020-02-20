package nl.tudelft.oopp.demo.objects.facility;

import nl.tudelft.oopp.demo.objects.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//This repository gets all data from the rooms table, and also queries to it
public interface FacilityRepository extends JpaRepository<Facility, Long> {


}
