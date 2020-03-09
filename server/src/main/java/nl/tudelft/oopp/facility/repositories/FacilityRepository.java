package nl.tudelft.oopp.facility.repositories;

import nl.tudelft.oopp.facility.entities.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//This repository gets all data from the rooms table, and also queries to it
public interface FacilityRepository extends JpaRepository<Facility, Long> {

}
