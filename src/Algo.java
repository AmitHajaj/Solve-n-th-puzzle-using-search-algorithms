import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Algo {
    private HashSet<String> operators = new HashSet<>();

    public Algo() {
        this.operators = new HashSet<>();
    }

    /**
     * BFS->>
     * @param start
     * @param goal
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public List<node> BFS_V(node start, node goal) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        LinkedList<node> L = new LinkedList<>();
        Hashtable<String, node> l = new Hashtable<>();
        L.add(start);
        int counter = 0;

        Hashtable<String, node> C = new Hashtable<>();

        Class nodeClass = node.class;
        while (!L.isEmpty()){
            node n = L.removeFirst();
            l.remove(n);
            C.put(n.toString(), n);

            Method m = node.class.getDeclaredMethod("allowedOperators");
            List<node> allowed = (List<node>) m.invoke(n);
            for(node node: allowed){
                counter++;
                if(node == null){
                    continue;
                }
                if(C.get(node.toString()) == null && l.get(node.toString()) == null){
                    if(node.equals(goal)){
                        System.out.println("Number of nodes created is:  " + counter);
                        return getPath(node);
                    }
                    else{
                        L.add(node);
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
//                            }
//                        }
//                    }
//                }
//            }
        }
        return null;
    }

    /**
     * Dijkstra->>
     * @param game
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<node> AStar(puzzle game) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Hashtable<String, node> C = new Hashtable<>();
        PriorityQueue<node> O = new PriorityQueue<>();
        Hashtable<String, node> O1 = new Hashtable<>();
        O.add(game.getCurrentState());
        O1.put(game.getCurrentState().toString(), game.getCurrentState());

        int counter=0;

        while (!O.isEmpty()){
            node q = O.poll(); // Get the cheapest state to explore.
            O1.remove(q.toString(), q);
            C.put(q.toString(), q); // Put it in the closed list.

            Method m = node.class.getDeclaredMethod("allowedOperators");
            List<node> allowed = (List<node>) m.invoke(q);
            // Iterate over all of the allowed operators.
            for(node node: allowed){
                counter++;
                node g = node;
                // If it is a node that were done exploring it-> ignore.
                if(C.contains(g) ){
                    continue;
                }
                else if(g.equals(game.getGoalState())){
                    System.out.println("Number of nodes created is:  " + counter);
                    System.out.println("Cost: "+(g.getPrevState().getF()+g.getCostToHere()) );
                    return getPath(g);
                }
                else if(!O1.contains(g)){
                    g.setCostToHere(g.getCostToHere() + q.getCostToHere());
                    g.setF(g.getCostToHere() + game.manhattan(g));
                    O.add(g);
                    O1.put(g.toString(), g);
                }
                else if(O1.contains(g)){
                    g = O1.get(g.toString());
                    // If this child is already in the open list, we check if it is closer from here then before.
                    if(g.getCostToHere() > node.getCostToHere() + q.getCostToHere()){
                        g.setCostToHere(node.getCostToHere() + q.getCostToHere());
                        g.setF(g.getCostToHere() + game.manhattan(g));
                        g.setPrevState(q);
                    }
                }
            }

        }
        return null;
    }

    /**
     * DFID->>
     * @param start
     * @param goal
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<node> DFID(node start, node goal) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for(int i=0; i<Integer.MAX_VALUE; i++){
            Hashtable<String, node> H = new Hashtable<>();
            String result = limited_DFS(start, goal, i, H);
            if(result != "cutOff"){
                return getPath(H.get(result));
            }
        }
        return null;
    }

    /**
     * Limited DFS->>
     * @param n
     * @param goal
     * @param limit
     * @param hash
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String limited_DFS(node n, node goal, int limit, Hashtable<String, node> hash) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(n.equals(goal)){
            hash.put(n.toString(), n);
            return n.toString();
        }
        else if(limit==0){
            return "cutOff";
        }
        else{
            hash.put(n.toString(), n);
            boolean isCutOff = false;
            Method m = node.class.getDeclaredMethod("allowedOperators");
            List<node> allowed = (List<node>) m.invoke(n);
            // Iterate over all of the allowed operators.
            for(node node: allowed){
                if(hash.containsKey(node.toString())){
                    continue;
                }
                String result = limited_DFS(node, goal, limit-1, hash);
                if(result == "cutOff"){
                    isCutOff = true;
                }
                else if(result != ""){
                    return result;
                }
            }
            hash.remove(n);
            if(isCutOff){
                return "cutOff";
            }
            else{
                return "";
            }
        }
    }

    /**
     * IDASTAR->>...
     * @param p - the puzzle we work on.
     * @return List of the path from start state to goal state
     */
    public List<node> IDAStar(puzzle p) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Stack<node> L= new Stack<>();
        Hashtable<String, node> H = new Hashtable<>();
        int count =0;

        int t = p.manhattan(p.getCurrentState());
        while (t != Integer.MAX_VALUE){
            int minF = Integer.MAX_VALUE;
            L.push(p.getCurrentState());
            H.put(p.getCurrentState().toString(), p.getCurrentState());
            while(!L.isEmpty()){
                node n = L.pop();
                if(n.isOut){
                   H.remove(n.toString());
                }
                else{
                    n.isOut =true;
                    L.push(n);
                    Method m = node.class.getDeclaredMethod("allowedOperators");
                    List<node> allowed = (List<node>) m.invoke(n);
                    // Iterate over all of the allowed operators.
                    for(node node: allowed){
                        count++;
                        node.setCostToHere(node.getCostToHere()+n.getCostToHere());
                        node.setF(node.getCostToHere()+p.manhattan(node));
                        if(node.getF()>t){
                            minF = Math.min(minF, node.getF());
                            continue;
                        }
                        if(H.containsKey(node.toString()) && H.get(node.toString()).isOut){
                            continue;
                        }
                        if(H.containsKey(node.toString()) && !H.get(node.toString()).isOut){
                            if(H.get(node.toString()).getF() > node.getF()){
                                L.remove(H.get(node.toString()));
                                H.remove(node.toString());
                            }
                            else{
                                continue;
                            }
                        }
                        if(node.equals(p.getGoalState())){
                            System.out.println("Num = " + count);
                            return getPath(node);
                        }
                        L.push(node);
                        H.put(node.toString(), node);
                    }
                }
            }
            p.getCurrentState().isOut = false;
            t = minF;
        }
        return null;
    }

    /**
     * DFBnB->>
     * @param p, the puzzle we work on
     * @return
     */
    public List<node> DFNBnB(puzzle p) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Stack<node> L = new Stack<>();
        Hashtable<String, node> H = new Hashtable<>();

        L.push(p.getCurrentState());
        H.put(p.getCurrentState().toString(), p.getCurrentState());

        List<node> result = null;
        int t = Integer.MAX_VALUE;
        while(!L.isEmpty()){
            node n = L.pop();
            if(n.isOut){
                H.remove(n.toString());
            }
            else{
                n.isOut = true;
                L.push(n);
                Method m = node.class.getDeclaredMethod("allowedOperators");
                List<node> allowed = (List<node>) m.invoke(n);
                for(node g:allowed){
                    g.setCostToHere(n.getCostToHere()+g.getCostToHere());
                    g.setF(g.getCostToHere() + p.manhattan(g));
                }
                Collections.sort(allowed);

                ListIterator<node> itr = allowed.listIterator(allowed.size());
                while(itr.hasPrevious()){
                    node g = itr.previous();
                    if(g.getF() >= t){
                        itr.remove();
                        itr.forEachRemaining(node->itr.remove());
                    }
                    else if(H.containsKey(g.toString()) && H.get(g.toString()).isOut){
                        itr.remove();
                    }
                    else if(H.containsKey(g.toString()) && !H.get(g.toString()).isOut){
                        if(H.get(g.toString()).getF() <= g.getF()){
                            itr.remove();
                        }
                        else{
                            H.remove(g.toString());
                            L.remove(g);
                        }
                    }
                    else if(g.equals(p.getGoalState())){
                        t = g.getF();
                        result = getPath(g);
                        itr.forEachRemaining(node->itr.remove());
                    }
                }
                Iterator<node> iter = allowed.iterator();
                iter.forEachRemaining(node-> {
                                                H.put(iter.next().toString(), iter.next());
                                                L.push(iter.next());
                                            });
            }
        }
        return result;
    }

    private node findMinInTable(Hashtable<String, node> t){
        int min = Integer.MAX_VALUE;
        node minNode = null;
        for(node n: t.values()){
            if(n.getCostToHere()< min){
                minNode = n;
            }
        }
        return minNode;
    }

    private static Stack<node> getPath(node target){
        node n = target;
        int cost = 0;
        Stack<node> path = new Stack<>();
        while(n.getPrevState() != null){
            cost+=n.getCostToHere();
            path.push(n);
            n = n.getPrevState();
        }
        System.out.println("Cost:  "+cost);
        return path;
    }

    private String printPath(Stack<node> path){
        String ans = "";
        while (!path.isEmpty()){
            ans+= path.pop().howIGotHere;
        }
        return ans;
    }

}
