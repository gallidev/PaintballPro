package ai;


import enums.GameMode;
import javafx.geometry.Point2D;
import rendering.Map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class used retrieve path information from a CSV file and store them in a hash map, where the keys are the are the start and end points of the path
 * @author Sivarjuen Ravichandran
 */
public class HashMapGen {

    private HashMap<PointPairs, ArrayList<Point2D>> pathMap;
    private String csvFile;
    private String line = "";
    private String csvSplitBy = ",";

    /**
     * Finds the relevant CSV file for a given map, and calls the method which generates the hash map
     * @param map The map
     */
    public HashMapGen(Map map){
        if(map.getGameMode() == GameMode.CAPTURE_THE_FLAG){
            csvFile = "res/maps/castle_paths.csv";
        } else {
            csvFile = "res/maps/desert_paths.csv";
        }

        pathMap = new HashMap<>();
        try{
            generatePathMap();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Generates a hash map with all possible paths from a csv file. The keys of the hash map are the start and end coordinates of the path
     * @throws Exception
     */
    private void generatePathMap() throws Exception{

        BufferedReader br = new BufferedReader(new FileReader(csvFile));

        while ((line = br.readLine()) != null) {

            // use comma as separator
            String[] path = line.split(csvSplitBy);

            PointPairs key = new PointPairs(Double.parseDouble(path[0]),Double.parseDouble(path[1]),Double.parseDouble(path[2]),Double.parseDouble(path[3]));
            int pathLength = (path.length - 4)/2;
            ArrayList<Point2D> values = new ArrayList<Point2D>();

            if(path.length > 4){
                for(int i = 4; i < path.length; i += 2){
                    values.add(new Point2D(Double.parseDouble(path[i]),Double.parseDouble(path[i+1])));
                }
            }
            pathMap.put(key, values);
        }
    }

    /**
     * Returns the generated hash map
     * @return
     */
    public HashMap<PointPairs, ArrayList<Point2D>> getPathMap(){
        return this.pathMap;
    }

}
