import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Stack;

public class Ex1 {
    public static void main(String[] args){
        int[][] goalState = {{1,2},{3,-1}};
        int[][] currentState = {{-1,3},{2,1}};

        node start = new node(currentState, null);
        node target = new node(goalState, null);

        HashMap<String, Integer> priceTable = new HashMap<>();
        priceTable.put("one", 5);
        priceTable.put("twoUp", 7);
        priceTable.put("twoDown", 7);
        priceTable.put("twoRight", 6);
        priceTable.put("twoLeft", 6);

        puzzle p = new puzzle(start, target, priceTable);
        Algo bfs = new Algo();
        try {
            Stack<node> path = (Stack<node>) bfs.BFS_V(start, target);
            while (!path.isEmpty()){
                System.out.println(path.pop().howIGotHere);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }



    }
}
