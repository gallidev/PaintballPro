package ai;


import enums.GameMode;
import javafx.geometry.Point2D;
import rendering.Map;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HashMapGen {

    private HashMap<PointPairs, ArrayList<Point2D>> pathMap;
    private HashMap<PointPairs, Boolean> coverMap;
    private String csvFile;
    private String csvFile2;
    private String line = "";
    private String csvSplitBy = ",";

    public HashMapGen(Map map){
        if(map.getGameMode() == GameMode.CAPTURETHEFLAG){
            csvFile = "res/maps/ctf_paths.csv";
            csvFile2 = "res/maps/ctf_coverVision.csv";
        } else {
            csvFile = "res/maps/elimination_paths.csv";
            csvFile2 = "res/maps/elimination_coverVision.csv";
        }

        pathMap = new HashMap<>();
        coverMap = new HashMap<>();
        try{
            generatePathMap();
            generateCoverMap();
        } catch(Exception e){
            System.out.println(e);
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

    private void generateCoverMap() throws Exception{

        BufferedReader br = new BufferedReader(new FileReader(csvFile2));

        while ((line = br.readLine()) != null) {

            // use comma as separator
            String[] cover = line.split(csvSplitBy);

            PointPairs key = new PointPairs(Double.parseDouble(cover[0]),Double.parseDouble(cover[1]),Double.parseDouble(cover[2]),Double.parseDouble(cover[3]));
            Boolean value = Boolean.parseBoolean(cover[4]);
            coverMap.put(key, value);
        }
    }

    public HashMap<PointPairs, ArrayList<Point2D>> getPathMap(){
        return this.pathMap;
    }

    public HashMap<PointPairs, Boolean> getCoverMap(){
        return this.coverMap;
    }



}
