package ai;


import javafx.application.Application;
import javafx.stage.Stage;
import rendering.Map;
import ai.Node;

import java.io.FileWriter;

public class GeneratePaths extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map map = Map.loadRaw("elimination") ;
        Pathfinding pathfinder = new Pathfinding(map);
        Node[][] nodes = pathfinder.getNodeGrid();
        int x = nodes.length;
        int y = nodes[0].length;

        String csvFile = "res/maps/" + "elimination" + "_paths.csv";
        FileWriter writer = new FileWriter(csvFile);

        //For every node, calculate a path to every other node
        //Both the start and end nodes should not be null (i.e props, walls, outside area)
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){

                //Remove start nodes that are null
                if(nodes[i][j] == null) continue;

                for(int ti = 0; ti < x; ti++){
                    for(int tj = 0; tj < y; tj ++){

                        //Remove end nodes that are null
                        if(nodes[ti][tj] == null) continue;
                        if(i == ti && j == tj) continue;
                        Path currentPath = pathfinder.getPath(i, j, ti, tj);
                        //System.out.println("Path length = " + currentPath.getLength());
                        //write i, j, ti and tj to file
                        String str = i + "," + j + "," + ti + "," + tj;

                        for(int n = 0; n < currentPath.getLength(); n++){
                            double tempX = currentPath.getX(n);
                            double tempY = currentPath.getY(n);
                            //write tempX and tempY at the end of the line
                            str = str + "," + (int)tempX + "," + (int)tempY;
                        }

                        //Write str to file
                        writer.write(str + "\n");
                    }
                }
            }
            System.out.println("Progress:" + (int)(((double)(i+1)/(x))*100) + "%");

        }

        writer.flush();
        writer.close();
        //System.exit(0);
    }
}
