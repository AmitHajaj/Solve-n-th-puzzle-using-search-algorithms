import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Ex1 {
    public static void main(String[] args) throws IOException {
        String algo = "";
        boolean withTime = true;
        boolean isOpen = true;
        String boardSize = "";
        int boardLength = 0;
        int boardWidth = 0;
        String startBoard = "";
        String goalBoard = "";


        File inFile = new File("input.txt");

        Scanner scan = new Scanner(inFile);

        //Get the algorithm we work on.
        //and name it like it named in the algo class.
        algo = scan.nextLine();
        if(algo.equals("A*")){
            algo = "AStar";
        }
        else if(algo.equals("IDA*")){
            algo = "IDAStar";
        }
        String time = scan.nextLine();
        if(time.equals("no time")){
            withTime = false;
        }
        String open = scan.nextLine();
        if(open.equals("no time")){
            isOpen = false;
        }

        //Get the board size and capitalized it.
        boardSize = scan.nextLine();
        boardSize = boardSize.replace("x", "X");

        boardLength = Integer.parseInt(boardSize.charAt(0)+"");
        boardWidth = Integer.parseInt(boardSize.charAt(2)+"");

        //make the start board as a string.
        String currLine = scan.nextLine();
        while(currLine.charAt(0) != 'G'){
            startBoard = startBoard.concat(currLine + "\n");
            currLine = scan.nextLine();
        }

        //make startBoard a node
        node start = stringToNode(startBoard, boardLength, boardWidth);

        //make the goal board as a string.
        currLine = scan.nextLine();
        for(int i=0; i<boardLength; i++){
            goalBoard = goalBoard.concat(currLine+"\n");
            if(scan.hasNextLine()){
                currLine = scan.nextLine();
            }
            else{
                break;
            }
        }

        //make goalBoard a node.
        node goal = stringToNode(goalBoard, boardLength, boardWidth);

        //pretty unnecessary
        HashMap<String, Integer> priceTable = new HashMap<>();
        priceTable.put("one", 5);
        priceTable.put("twoUp", 7);
        priceTable.put("twoDown", 7);
        priceTable.put("twoRight", 6);
        priceTable.put("twoLeft", 6);

        //initial a puzzle and apply the algorithm on it.
        puzzle p = new puzzle(start, goal, priceTable);
        Algo algoRun = new Algo();
        long st = System.nanoTime();
        File path = null;
        try {
            Method m = Algo.class.getDeclaredMethod(algo, p.getClass());
            path = (File) m.invoke(algoRun, p);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        if(withTime) {
            Files.write(Paths.get("output.txt"),("time: "+(end - st) / Math.pow(10, 9)).getBytes(), StandardOpenOption.APPEND);
        }
    }

    static node stringToNode(String state, int length, int width){
        String[][] ansString = new String[length][width];
        String[] lines = state.split("\n");
        for(int i=0; i<lines.length; i++){
            lines[i] = lines[i].replace("_", "-1");
            ansString[i] = lines[i].split(",");
        }

        //Now we will create to node object
        int[][] stateArr = new int [length][width];
        for(int i=0; i<length; i++){
            for(int j=0; j<width; j++){
                if(ansString[i][j].equals("-1")){
                    stateArr[i][j] = -1;
                }
                else {
                    stateArr[i][j] = Integer.parseInt(ansString[i][j]);
                }
            }
        }

        node ans = new node(stateArr, null);
        return ans;
    }
}
