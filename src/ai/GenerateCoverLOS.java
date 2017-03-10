package ai;


import javafx.application.Application;
import javafx.stage.Stage;
import rendering.Map;
import ai.Node;

import java.io.FileWriter;

public class GenerateCoverLOS extends Application {

    public static void main(String[] args) throws Exception {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Arguments - name of map
        Map map = Map.loadRaw("ctf");
        CoverVision cvision = new CoverVision(map);
        Node[][] nodes = cvision.getNodeGrid();
        PropNode[] props = cvision.getPropNodes();

        int n = props.length;
        int x = nodes.length;
        int y = nodes[0].length;

        String csvFile = "res/maps/" + "ctf" + "_coverVision.csv";
        FileWriter writer = new FileWriter(csvFile);

        //For every possible cover node, compute whether there is LoS with a given node
        //Both the start and end nodes should not be null (i.e props, walls, outside area)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {

                int xCoord = props[i].x;
                int yCoord = props[i].y;

                //0 = Up
                //1 = Down
                //2 = Left
                //3 = Right
                switch (j) {
                    case 0:
                        yCoord--;
                        break;
                    case 1:
                        yCoord++;
                        break;
                    case 2:
                        xCoord--;
                        break;
                    case 3:
                        xCoord++;
                        break;
                }

                //Remove start nodes that are null
                if (nodes[xCoord][yCoord] == null) continue;

                for (int ti = 0; ti < x; ti++) {
                    for (int tj = 0; tj < y; tj++) {

                        //Remove end nodes that are null
                        if (nodes[ti][tj] == null) continue;
                        if (i == ti && j == tj) continue;

                        Boolean inVision = cvision.inSight(xCoord, yCoord, ti, tj);
                        //write xCoord, yCoord, ti, tj and inVision to file
                        String str = xCoord + "," + yCoord + "," + ti + "," + tj + "," + inVision;

                        //Write str to file
                        writer.write(str + "\n");
                    }
                }
                System.out.println("Progress:" + (int) (((double) (i + 1) / (n)) * 100) + "%");

            }


        }
        writer.flush();
        writer.close();
        System.exit(0);
    }
}
