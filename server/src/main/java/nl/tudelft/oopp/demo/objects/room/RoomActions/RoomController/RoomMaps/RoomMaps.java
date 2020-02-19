package nl.tudelft.oopp.demo.objects.room.RoomActions.RoomController.RoomMaps;

import nl.tudelft.oopp.demo.objects.building.BuildingRepository;
import nl.tudelft.oopp.demo.objects.room.RoomActions.RoomController.RoomController;
import nl.tudelft.oopp.demo.objects.room.RoomRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class RoomMaps extends RoomController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    //Mapped function to add a new room
    @GetMapping("addRoom")
    @ResponseBody
    public String AddRoom(@RequestParam int capacity, @RequestParam boolean onlyStaff, @RequestParam int[] facilities,@RequestParam int buildingId, @RequestParam String description) {
        addRoom(16,true,facilities,1,"test2");
        return "saved";
    }
}
