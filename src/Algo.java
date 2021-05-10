import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Algo {
    static int counter = 0;
    private HashSet<String> operators = new HashSet<>();
    File output;

    public Algo() {
        this.operators = new HashSet<>();
    }

    /**
     * BFS->>
     * @param p, the puzzle we work on.
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public File BFS(puzzle p) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        node start = p.getCurrentState(), goal = p.getGoalState();
        LinkedList<node> L = new LinkedList<>();
        Hashtable<String, node> l = new Hashtable<>();
        L.add(start);
        l.put(start.toString(), start);

        Hashtable<String, node> C = new Hashtable<>();

        while (!L.isEmpty()){
            node n = L.removeFirst();
            l.remove(n);
            C.put(n.toString(), n);

            Method m = node.class.getDeclaredMethod("allowedOperators");
            List<node> allowed = (List<node>) m.invoke(n);
            //Before exploring them, check if one of them is the goal.
            for(node node: allowed){
                if(node.equals(goal)){
                    return getPath(node);
                }
            }

            for(node node: allowed){
                counter++;
                if(!C.containsKey(node.toString()) && !l.containsKey(node.toString())){
                    if(node.equals(goal)){
                        return getPath(node);
                    }
                    else{
                        L.add(node);
                        l.put(node.toString(), node);
                    }
                }
            }
        }
        return null;
    }

    /**
     * AStar->>
     * @param p, the puzzle we work on
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public File AStar(puzzle p) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        node start = p.getCurrentState(), goal = p.getGoalState();
        Hashtable<String, node> C = new Hashtable<>();
        PriorityQueue<node> L = new PriorityQueue<>();
        Hashtable<String, node> L1 = new Hashtable<>();

        start.setH(goal);

        L.add(start);
        L1.put(start.toString(), start);
        while(!L.isEmpty()){
            node n = L.poll();
            if(n.equals(goal)){
                return getPath(n);
            }
            C.put(n.toString(), n);

            Method m = node.class.getDeclaredMethod("allowedOperators");
            List<node> allowed = (List<node>) m.invoke(n);
            // Iterate over all of the allowed operators.
            for(node node: allowed) {
                counter++;
                node x = node;
                x.setH(goal);
                x.setF(x.getCostToHere()+x.getH());
                if(!C.containsKey(x.toString()) && !L1.containsKey(x.toString())){
                    L.add(x);
                    L1.put(x.toString(), x);
                }
                else if(L1.containsKey(x.toString())){
                    if(L1.get(x.toString()).getF() > x.getF()){
                        L.remove(x);
                        L.add(x);
                        L1.put(x.toString(), x);
                    }
                }
            }
        }
        return null;
    }

    /**
     * DFID->>
     * @param p, the puzzle we work on
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public File DFID(puzzle p) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        node start = p.getCurrentState(), goal = p.getGoalState();
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
    public File IDAStar(puzzle p) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        node start = p.getCurrentState();
        node goal = p.getGoalState();
        Stack<node> L= new Stack<>();
        Hashtable<String, node> H = new Hashtable<>();
        start.setH(goal);
        int t = start.getH();

        while (t != Integer.MAX_VALUE){
            int minF = Integer.MAX_VALUE;
            L.push(start);
            H.put(start.toString(), start);
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
                    // Iterate over all of the allowed operators.
                    for(node node: allowed){
                        counter++;
                        node.setH(goal);
                        node.setF(node.getCostToHere()+node.getH());
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
                        if(node.equals(goal)){
                            return getPath(node);
                        }
                        L.push(node);
                        H.put(node.toString(), node);
                    }
                }
            }
            start.isOut = false;
            t = minF;
        }
        return null;
    }

    /**
     * DFBnB->>
     * @param p, the puzzle we work on
     * @return
     */
    public File DFBnB(puzzle p) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Stack<node> L = new Stack<>();
        Hashtable<String, node> H = new Hashtable<>();

        L.push(p.getCurrentState());
        H.put(p.getCurrentState().toString(), p.getCurrentState());

        File result = null;
        int t = Integer.MAX_VALUE, cnt = 1;
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
                for(node g : allowed){
                    g.setH(p.getGoalState());
                    g.setF(g.getCostToHere() + g.getH());
                }
                Collections.sort(allowed);

                ListIterator<node> itr = allowed.listIterator();

                while(itr.hasNext()){
                    counter++;
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

    private static File getPath(node target) throws IOException {
        File out = new File("output.txt");
        FileWriter fw = new FileWriter(out);
        PrintWriter pw = new PrintWriter(fw);

        String rout = "";

        node n = target;
        int cost = 0;
        Stack<node> path = new Stack<>();
        while(n.getPrevState() != null){
            cost += (n.getCostToHere()-n.getPrevState().getCostToHere()) ;
            path.push(n);
            n = n.getPrevState();
        }

        //print the path
        while (!path.isEmpty()){
            node temp = path.pop();
            if(path.isEmpty()){
               rout = rout.concat(temp.howIGotHere);
            }
            else {
                rout = rout.concat(temp.howIGotHere + "-");
            }
        }
        pw.println(rout);
        pw.println("Num: "+counter);
        counter = 0;
        pw.println("Cost:  "+cost);
        pw.close();
        return out;
    }

    private String printPath(Stack<node> path){
        String ans = "";
        while (!path.isEmpty()){
            ans+= path.pop().howIGotHere;
        }
        return ans;
    }

}
