package nl.tudelft.oopp.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.tudelft.oopp.demo.entities.Quote;
import nl.tudelft.oopp.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuoteController {

    @Autowired
    private QuoteRepository quoteRepository;
    /**
     * GET Endpoint to retrieve a random quote.
     *
     * @return randomly selected {@link Quote}.
     */
    @GetMapping("quote")
    @ResponseBody
    public Quote getRandomQuote() {
        Quote q1 = new Quote(
                1,
                "A clever person solves a problem. A wise person avoids it.",
                "Albert Einstein"
        );

        Quote q2 = new Quote(
                2,
                "The computer was born to solve problems that did not exist before.",
                "Bill Gates"
        );

        Quote q3 = new Quote(
                3,
                "Tell me and I forget.  Teach me and I remember.  Involve me and I learn.",
                "Benjamin Franklin"
        );

        ArrayList<Quote> quotes = new ArrayList<>();
        quotes.add(q1);
        quotes.add(q2);
        quotes.add(q3);

        List<Quote> quotes2 = quoteRepository.findAll();

        return quotes.get(new Random().nextInt(quotes.size()));
    }
}



//    // Method to filter rooms based on capacity, a room being accessible to students or not, the facilities that
//    // should be present (if so their facility ids should be in the facilities array), the building name and a
//    // string of the inputted location
//    public List<Room> filterRooms(int capacity, boolean onlyStaff, int[] facilities, String building, String location) {
//        //Gets all room ids
//        int[] roomIds = roomRepository.getAllRoomIds();
//        //Makes a temp Array to store all suitable rooms in (their ids)
//        List<Integer> resRoomIds = new ArrayList<>();
//        int[] buildingIds = {};
//        boolean allFacs = true;
//        //Checks all the rooms individually to check if they are compatible with the inputted filters
//        for(int i=0; i<roomIds.length;i++) {
//            //If a building was inputted check if the current iteration of the room ids has a building name like
//            //the inputted building name (filter)
//            if(!building.equals("")) {
//                //Returns all buildings (their ids) that have names that are similar to the inputted building
//                //name filter
//                buildingIds = buildingRepository.filterBuildingsOnName(building);
//                allFacs = false;
//                //If the current room is in one of the buildings that has a name similar to the inputted filter
//                //, allFacs is set to true, if not, allFacs will be kept as false and later on the room wont be
//                //shown as being suitable
//                if(buildingIds.length > 0) {
//                    for (int a = 0; a < buildingIds.length; a++) {
//                        if (buildingIds[a] == roomRepository.getRoomById(roomIds[i]).getBuilding()) {
//                            allFacs = true;
//                        }
//                    }
//                }
//            }
//            //If all previous filters applied, check for these next filters
//            if (allFacs) {
//                //If a location was inputted check if the current iteration of the room ids has a building location
//                //like the inputted building location (filter)
//                if(!location.equals("")) {
//                    //Returns all buildings (their ids) that have locations that are similar to the inputted building
//                    //location filter
//                    buildingIds = buildingRepository.filterBuildingsOnLocation(location);
//                    allFacs = false;
//                    //If the current room is in one of the buildings that has a location similar to the inputted filter
//                    //, allFacs is set to true, if not, allFacs will be kept as false and later on the room wont be
//                    //shown as being suitable
//                    if(buildingIds.length > 0) {
//                        for (int a = 0; a < buildingIds.length; a++) {
//                            if (buildingIds[a] == roomRepository.getRoomById(roomIds[i]).getBuilding()) {
//                                allFacs = true;
//                            }
//                        }
//                    }
//                }
//                //Checks the room for having the required facilities
//                if(facilities.length > 0) {
//                    for (int a = 0; a < facilities.length; a++) {
//                        //If the table for relations between rooms having facilities returns null for the combination
//                        //of the current room and the required facility returns null this means the room doesnt have
//                        //the required facility
//                        if (roomFacilityRepository.filterRooms(roomIds[i], facilities[a]).size() == 0) {
//                            allFacs = false;
//                        }
//                    }
//                }
//            }
//            //If all previous filters applied, input the room into the integer array to later return all suitable rooms
//            if(allFacs) {
//                resRoomIds.add(roomIds[i]);
//            }
//        }
//        //After checking all rooms return all the suitable ones if they match the required capacity, onlyStaff value
//        //and all the previous filters also applied (which would have put them in the integer resRoomIds, so we check
//        //if the rooms have ids that are also in resRoomIds)
//        List<Room> rooms = roomRepository.filterRooms(11, true, resRoomIds);
//        return rooms;
//    }
