import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Algo {
    private HashSet<String> operators = new HashSet<>();

    public Algo() {
//        String[] operator = {"up", "down", "left", "right", "twoUp", "twoDown", "twoRight", "twoLeft"};
        this.operators = new HashSet<>();
//        this.operators.addAll(Arrays.asList(operator));
    }

    public List<node> BFS_V(node start, node goal) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        LinkedList<node> L = new LinkedList<>();
        Hashtable<String, node> l = new Hashtable<>();
        L.add(start);

        Hashtable<String, node> C = new Hashtable<>();

        Class nodeClass = node.class;
        while (!L.isEmpty()){
            node n = L.removeFirst();
            l.remove(n);
            C.put(n.toString(), n);

            Method m = node.class.getDeclaredMethod("allowedOperators");
            List<node> allowed = (List<node>) m.invoke(n);
            for(node node: allowed){
                if(node == null){
                    continue;
                }
                node g = node;
                if(C.get(g.toString()) == null && l.get(g.toString()) == null){
                    if(g.equals(goal)){
                        return getPath(g);
                    }
                    else{
                        L.add(g);
                        // TODO: think how to set L as a priority queue based on the cost and the created iteration...
                    }
                }
            }

//            for(Method operator: nodeClass.getMethods()){
//                boolean isOperator = operator.getName() == "allowedOperators";
//                if(isOperator) {
//                    for (int i = 0; i<2 && n.locations[i] != null; i++ ) {
//                        node g = (node) operator.invoke(n, n.locations[i]);
//                        //If it is not allowed operator
//                        if(g == null){
//                            continue;
//                        }
//                        if(C.get(g.toString()) == null && l.get(g.toString()) == null){
//                            if(g.equals(goal)){
//                                return getPath(g);
//                            }
//                            else{
//                                L.add(g);
//                                // TODO: think how to set L as a priority queue based on the cost and the created iteration...
//                            }
//                        }
//                    }
//                }
//            }
        }
        return null;
    }


    private static Stack<node> getPath(node target){
        node n = target;
        Stack<node> path = new Stack<>();
        while(n.getPrevState() != null){
            path.push(n);
            n = n.getPrevState();
        }
        return path;
    }
}
