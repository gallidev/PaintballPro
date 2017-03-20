package ai;


import enums.GameMode;
import javafx.geometry.Point2D;
import rendering.Map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class HashMapGen {

    private HashMap<PointPairs, ArrayList<Point2D>> pathMap;
    private String csvFile;
    private String line = "";
    private String csvSplitBy = ",";

    public HashMapGen(Map map){
        if(map.getGameMode() == GameMode.CAPTURETHEFLAG){
            csvFile = "res/maps/ctf_paths.csv";
        } else {
            csvFile = "res/maps/elimination_paths.csv";
        }

        pathMap = new HashMap<>();
        try{
            generatePathMap();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

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

    public HashMap<PointPairs, ArrayList<Point2D>> getPathMap(){
        return this.pathMap;
    }

}
