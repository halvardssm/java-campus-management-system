package nl.tudelft.oopp.group39.facility.repositories;

import nl.tudelft.oopp.group39.facility.entities.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//This repository gets all data from the rooms table, and also queries to it
public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
