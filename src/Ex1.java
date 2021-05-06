import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Ex1 {

    public static void main(String[] args) throws FileNotFoundException {
        String algo = "";
        boolean withTime = true;
        boolean isOpen = false;
        String boardSize = "";
        int boardLength = 0;
        int boardWidth = 0;
        String startBoard = "";
        String goalBoard = "";



//        File inFile = null;
//        if (0 < args.length) {
//            inFile = new File(args[0]);
//        } else {
//            System.err.println("Invalid arguments count:" + args.length);
//            System.exit(-3);
//        }
        File inFile = new File("C:\\Users\\Amit Hajaj\\IdeaProjects\\deafult_package\\src\\input.txt");

        Scanner scan = new Scanner(inFile);

        algo = scan.nextLine();
        if(scan.nextLine().charAt(0) != 'w'){
            withTime = false;
        }
        if(scan.nextLine().charAt(0) != 'n'){
            isOpen = true;
        }

        //Get the board size and capitalized it.
        boardSize = scan.nextLine();
        boardSize = boardSize.replace("x", "X");

        boardLength = Integer.parseInt(boardSize.charAt(0)+"");
        boardWidth = Integer.parseInt(boardSize.charAt(2)+"");

        String currLine = scan.nextLine();
        while(currLine.charAt(0) != 'G'){
            startBoard = startBoard.concat(currLine + "\n");
            currLine = scan.nextLine();
        }

        //make startBoard a node
        node start = stringToNode(startBoard, boardLength, boardWidth);

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

        HashMap<String, Integer> priceTable = new HashMap<>();
        priceTable.put("one", 5);
        priceTable.put("twoUp", 7);
        priceTable.put("twoDown", 7);
        priceTable.put("twoRight", 6);
        priceTable.put("twoLeft", 6);

        puzzle p = new puzzle(start, goal, priceTable);
        Algo bfs = new Algo();
        long st = System.nanoTime();
        try {
            Stack<node> path = (Stack<node>) bfs.AStar(p.getCurrentState(), p.getGoalState());
            while (path != null && !path.isEmpty()){
                System.out.println(path.pop().howIGotHere);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        if(withTime) {
            System.out.println((end - st) / Math.pow(10, 9));
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
