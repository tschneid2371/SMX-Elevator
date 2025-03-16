import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class Main {
    public static List<Integer> convertToInt(List<String> stringList) {
        List<Integer> intList = new ArrayList<>();
        for (String str : stringList) {
            try {
                // String strWithoutSpace1 = field.replaceAll("\\s", "");
                intList.add(Integer.parseInt(str.replaceAll("\\s", "")));
            } catch (NumberFormatException e) {
                System.err.println("Invalid integer format: " + str);
                // Handle the exception as needed, e.g., skip the element or set a default value
            }
        }
        return intList;
    }
    
    // An EXAMPLE of optimized sort (only accurate if everyone enters on same floor and start selecting buttons)
    // to optimize, would need to know where folk entered, then what the selected
    public static void optimizedForAllFolkEnterOnStartingFloor(
        Integer startingFloor, List<Integer> integerList) {
            // System.out.println(integerList);
            integerList.sort(null);
            // System.out.println(integerList);
            // Is start closer to first or last?
            int distanceToLowest =  Math.abs(startingFloor - integerList.get(0));
            int distanceToHighest = Math.abs(startingFloor - integerList.get(integerList.size()-1));
            // System.out.println("distance to lowest " + distanceToLowest + " distance to highest " + distanceToHighest);
            if (distanceToLowest >= distanceToHighest)
              integerList.sort(Comparator.reverseOrder());
            performCalculation(startingFloor, integerList);
    }
    
    // print out result
    public static void performCalculation(Integer startingFloor, 
      List<Integer> integerList) {
        Integer totalTravelTime=0, current=0, next=0;
        current = startingFloor;
        for (Integer floor : integerList) {
            next = floor;
            if(current > next) 
              totalTravelTime += (current-next)*FLOOR_DURATION_SECS;
            else if (next > current) 
              totalTravelTime += (next-current)*FLOOR_DURATION_SECS;
            // System.out.println("floor visited " + floor + " duration total " + totalTravelTime);
            current=next;
        }
        System.out.println("OPTIMED TRAVEL TIME: Starting at floor " + startingFloor + 
                " traveling " + integerList + " took " + totalTravelTime + " secs");
    }
        
    public static int FLOOR_DURATION_SECS = 10;
    public static void main(String[] args) {
        int floorDurationSecs = 10;
        String filePath = "Input.csv";
        TreeMap<String, List<String> > lLACoorTreeMap = new TreeMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Integer entry = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                entry++;
                String[] strArray = line.split(",");
                
                // back to back numbers skipped
                // strip spaces from beginning and end of string
                // NOTE: some error checking (at least 2 entries)
                if (strArray.length < 2) { 
                    System.out.println("Not enough floors provided (minimum of 2) skipping entry " + Arrays.toString(strArray));
                    continue;
                }
                if (strArray.length > 10) { 
                    System.out.println("Too many floors provided (maximum of 10) skipping entry " + Arrays.toString(strArray));
                    continue;
                }
                List<String> floorVisits = new ArrayList<>(Arrays.asList(strArray));
                List<Integer> floorVisitsInt = convertToInt(floorVisits);
                // If any value greater than 32 floors
                if(floorVisitsInt.stream().anyMatch(n -> n > 32)) {
                    System.out.println("Invalid floor provided skipping entry " + Arrays.toString(strArray));
                    continue;
                }
                List<Integer> remainingFloorsInt = 
                  new ArrayList<>(floorVisitsInt.subList(1, floorVisitsInt.size()));
                // non-optimized duration
                Integer startingFloor = floorVisitsInt.get(0);
                Integer totalTravelTime=0, current=0, next=0;
                current = startingFloor;
                for (Integer floor : remainingFloorsInt) {
                    next = floor;
                    if(current > next) 
                      totalTravelTime += (current-next)*floorDurationSecs;
                    else if (next > current) 
                      totalTravelTime += (next-current)*floorDurationSecs;
                    
                    // System.out.println("floor visited " + floor + " duration total " + totalTravelTime);
                    current=next;
                }
                // System.out.println("Entry " + entry + " duration " + totalTravelTime);
                System.out.println("RAW TRAVEL TIME: Starting at floor " + startingFloor + " traveling " + remainingFloorsInt + 
                  " took " + totalTravelTime + " secs");
                
                optimizedForAllFolkEnterOnStartingFloor(startingFloor, remainingFloorsInt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
