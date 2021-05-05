import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Stack;

public class Ex1 {

    public static void main(String[] args){
        long st = System.nanoTime();
        int[][] currentState1 = {{1,2,3,4},
                                {5,6,11,7},
                                {9,10,8,-1}
                                };
        int[][] goalState1 = {{1,2,3,4},
                            {5,6,7,8},
                            {9,10,11,-1}
                            };

        int[][] goalState2 = {{1,2,3},
                                {4,5,6},
                                {7,-1,-1}
        };
        int[][] currentState2 = {{1,-1,4},
                            {3,5,6},
                            {2,-1,7}
        };

        node start = new node(currentState2, null);
        node target = new node(goalState2, null);

        HashMap<String, Integer> priceTable = new HashMap<>();
        priceTable.put("one", 5);
        priceTable.put("twoUp", 7);
        priceTable.put("twoDown", 7);
        priceTable.put("twoRight", 6);
        priceTable.put("twoLeft", 6);

        puzzle p = new puzzle(start, target, priceTable);
        Algo bfs = new Algo();
        try {
            Stack<node> path = (Stack<node>) bfs.AStar2(p);
            while (path != null && !path.isEmpty()){
                System.out.println(path.pop().howIGotHere);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();

        System.out.println((end-st)/Math.pow(10, 9));
    }
}
