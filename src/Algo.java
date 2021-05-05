import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Algo {
    static int counter = 0;
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
        l.put(start.toString(), start);
        int counter = 1;

        Hashtable<String, node> C = new Hashtable<>();

        while (!L.isEmpty()){
            node n = L.removeFirst();
            l.remove(n);
            C.put(n.toString(), n);

            Method m = node.class.getDeclaredMethod("allowedOperators");
            List<node> allowed = (List<node>) m.invoke(n);
            for(node node: allowed){
                counter++;
                if(!C.containsKey(node.toString()) && !l.containsKey(node.toString())){
                    if(node.equals(goal)){
                        System.out.println("Num:  " + counter);
                        return getPath(node);
                    }
                    else{
                        L.add(node);
                        l.put(node.toString(), node);
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
     * AStar->>
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
                    O.remove(g);
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
     * AStar->>
     * @param game
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<node> AStar2(puzzle game) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int counter = 1;
        Hashtable<String, node> C = new Hashtable<>();
        PriorityQueue<node> L = new PriorityQueue<>();
        Hashtable<String, node> L1 = new Hashtable<>();

        L.add(game.getCurrentState());
        L1.put(game.getCurrentState().toString(), game.getCurrentState());
        while(!L.isEmpty()){
            node n = L.poll();
            n.setH(game.getGoalState());
            if(n.equals(game.getGoalState())){
                System.out.println("Num: " + counter);
                return getPath(n);
            }
            C.put(n.toString(), n);

            Method m = node.class.getDeclaredMethod("allowedOperators");
            List<node> allowed = (List<node>) m.invoke(n);
            // Iterate over all of the allowed operators.
            for(node node: allowed) {
                counter++;
                node x = node;
                x.setH(game.getGoalState());
                x.setF(x.getCostToHere()+x.getH());
                if(!C.containsKey(x.toString()) && !L1.containsKey(x.toString())){
                    L.add(x);
                    L1.put(x.toString(), x);
                }
                else if(L1.containsKey(x.toString())){
                    if(L1.get(x.toString()).getF() > x.getF()){
                        L.remove(x);
                        L.add(x);
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
                System.out.println(counter);
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
                counter++;
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

                ListIterator<node> itr = allowed.listIterator();
                while(itr.hasNext()){
                    node g = itr.next();
                    if(g.getF() >= t){
                        itr.remove();
                        while(itr.hasNext() && allowed.contains(itr.next())){
                            itr.remove();
                        }
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
                        while(itr.hasNext() && allowed.contains(itr.next())){
                            itr.remove();
                        }
                    }
                }
                ListIterator<node> iter = allowed.listIterator(allowed.size());
                while(iter.hasPrevious()){
                    node g = iter.previous();
                    H.put(g.toString(), g);
                    L.push(g);
                }
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
            cost += (n.getCostToHere()-n.getPrevState().getCostToHere()) ;
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
