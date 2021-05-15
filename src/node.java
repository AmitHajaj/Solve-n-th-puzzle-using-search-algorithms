import java.util.*;

public class node implements Comparable<node> {
    private int id;
    private int[][] state;
    private node prevState;
    String howIGotHere;
    private int costToHere;
    private int f;
    private int h;
    List<location> locations;
    private static int stateCounter = 0; //Keeps track on the states that created. mainly for comparator.
    boolean isOut;


    public node(int[][] state, node prev) {
        this.id = id;
        this.isOut = false;
        this.state = state;
        this.prevState = prev;
        this.howIGotHere = "";
        this.locations = new LinkedList<>();
        this.costToHere = 0;
        this.id = stateCounter++;
        int cnt = 0;
        for(int i=0; i<state.length; i++){
            for(int j=0; j<state[i].length; j++){
                if(state[i][j] == -1){
                    if(cnt == 0) {
                        this.locations.add(new location(j, i));
                        cnt++;
                    }
                    else if(cnt == 1){
                        this.locations.add(new location(j, i));
                    }
                }
            }
        }
        Collections.sort(this.locations);
    }

    public int getF() {
        return f;
    }

    public int getH() {
        return h;
    }

    public void setH(node goal) {
        if(this.locations.size() == 1){
            this.h = this.normalManhattan(goal);
        }
        else {
            this.h = this.manhattan1(goal);
        }
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[][] getState() {
        return state;
    }

    public void setState(int[][] state) {
        this.state = state;
    }

    public node getPrevState() {
        return prevState;
    }

    public void setPrevState(node prevState) {
        this.prevState = prevState;
    }

    public int getCostToHere() {
        return costToHere;
    }

    public void setCostToHere(int costToHere) {
        this.costToHere = costToHere;
    }

    /**
     * In case there is two blank tiles on the puzzle, this method determine if they are joint.
     * @return 1 - joint horizontally
     *         -1 - joint vertically
     *         0 - not jointed at all or there is only one blank tile.
     */
    public int isJoint(){
        if(this.locations.size() == 1){
            return 0; //Only one blank tile.
        }
        else{
            for(int i = 0; i<this.locations.size();){
                if(locations.get(i).getX() == locations.get(i+1).getX()){
                    if(Math.abs(locations.get(i).getY() - locations.get(i+1).getY()) == 1){
                        return -1; //Horizontal jointed.
                    }
                    else{
                        return 0;
                    }
                }
                else if(locations.get(i).getY() == locations.get(i+1).getY()){
                    if(Math.abs(locations.get(i).getX() - locations.get(i+1).getX()) == 1){
                        return 1; //Vertically jointed.
                    }
                    else{
                        return 0;
                    }
                }
                else{
                    break;
                }
            }
        }
        return 0; //If there is two blank tiles but not jointed.
    }

    /**
     * for all valid operators on this state, this method will return an array of the possible states
     * and their cost.
     * The cost weighted based on the heuristic function that we choose.
     * @return array of possible states.
     */
    public List<node> allowedOperators(){
        int isJoint = this.isJoint();
        ArrayList<node> temp = new ArrayList<>();
        ArrayList<node> ans = new ArrayList<>();

        if(isJoint == -1){
            temp.add(this.twoLeft());
            temp.add(this.twoRight());
        }

        else if(isJoint == 1){
            temp.add(this.twoUp());
            temp.add(this.twoDown());
        }

        for(int i=0; i<this.locations.size(); i++) {
            temp.add(this.left(this.locations.get(i)));
            temp.add(this.up(this.locations.get(i)));
            temp.add(this.right(this.locations.get(i)));
            temp.add(this.down(this.locations.get(i)));
        }


        for(node n : temp){
            if(n != null && !ans.contains(n) && !n.equals(this)){
                ans.add(n);
            }
        }
        return ans;
    }

    /**
     * Move the blank tile 1 step down. If possible.
     * cost-> 5
     * @param l- the location of the blank tile.
     * @return the state which we move to.
     */
    public node down(location l){
        if(l.getY() == this.state.length-1 || this.state[l.getY() + 1][l.getX()] == -1){
            return null;
        }
        else {
            node ans = this.clone();
            int temp = this.state[l.getY()][l.getX()];
            ans.state[l.getY()][l.getX()] = this.state[l.getY() + 1][l.getX()];
            ans.state[l.getY() + 1][l.getX()] = temp;

            //set new location:
            for(location ls: ans.locations){
                if(ls.getY() == l.getY() && ls.getX() == l.getX()){
                    ls.setY(l.getY()+1);
                }
            }

            ans.prevState = this;
            ans.howIGotHere = "" + ans.state[l.getY()][l.getX()] + "U";
            ans.setCostToHere(this.getCostToHere()+5);

            if (ans.equals(this.prevState)) {
                return null;
            }
            else {
                return ans;
            }
        }
    }
    /**
     * Move the blank tile 1 step up. If possible.
     * cost-> 5
     * @param l- the location of the blank tile.
     * @return the state which we move to.
     */
    public node up(location l){
        if(l.getY() == 0 || this.state[l.getY() - 1][l.getX()] == -1){
            return null;
        }
        else{
            node ans = this.clone();
            int temp = this.state[l.getY()][l.getX()];
            ans.state[l.getY()][l.getX()] = this.state[l.getY()-1][l.getX()];
            ans.state[l.getY()-1][l.getX()] = temp;

            //set new location:
            for(location ls: ans.locations){
                if(ls.getY() == l.getY() && ls.getX() == l.getX()){
                    ls.setY(l.getY()-1);
                }
            }

            ans.prevState = this;
            ans.howIGotHere = "" + ans.state[l.getY()][l.getX()] + "D";
            ans.setCostToHere(this.getCostToHere()+5);
            if(ans.equals(this.prevState)) {
                return null;
            }
            else{
                return ans;
            }
        }
    }
    /**
     * Move the blank tile 1 step left. If possible.
     * cost-> 5
     * @param l- the location of the blank tile.
     * @return the state which we move to.
     */
    public node left(location l){
        if(l.getX() == 0 || this.state[l.getY()][l.getX()-1] == -1){
            return null;
        }
        else{
            node ans = this.clone();
            int temp = this.state[l.getY()][l.getX()];
            ans.state[l.getY()][l.getX()] = this.state[l.getY()][l.getX()-1];
            ans.state[l.getY()][l.getX()-1] = temp;

            //set new location:
            for(location ls: ans.locations){
                if(ls.getY() == l.getY() && ls.getX() == l.getX()){
                    ls.setX(l.getX()-1);
                }
            }


            ans.prevState = this;
            ans.howIGotHere = "" + ans.state[l.getY()][l.getX()] + "R";
            ans.setCostToHere(this.getCostToHere()+5);
            if(ans.equals(this.prevState)) {
                return null;
            }
            else{
                return ans;
            }
        }
    }
    /**
     * Move the blank tile 1 step right. If possible.
     * cost-> 5
     * @param l- the location of the blank tile.
     * @return the state which we move to.
     */
    public node right(location l){
        if(l.getX() == this.state[0].length-1 || this.state[l.getY()][l.getX()+1] == -1){
            return null;
        }
        else{
            node ans = this.clone();
            int temp = this.state[l.getY()][l.getX()];
            ans.state[l.getY()][l.getX()] = this.state[l.getY()][l.getX()+1];
            ans.state[l.getY()][l.getX()+1] = temp;

            //set new location:
            for(location ls: ans.locations){
                if(ls.getY() == l.getY() && ls.getX() == l.getX()){
                    ls.setX(l.getX()+1);
                }
            }

            ans.prevState = this;
            ans.howIGotHere = "" + ans.state[l.getY()][l.getX()] + "L";
            ans.setCostToHere(this.getCostToHere()+5);
            if(ans.equals(this.prevState)) {
                return null;
            }
            else{
                return ans;
            }
        }
    }
    /**
     * Move the two jointed blank tiles 1 step right. If possible.
     * cost-> 6
     * @return the state which we move to.
     */
    public node twoRight(){
        if(this.locations.get(0).getX() == this.state[0].length-1){
            return null;
        }
        else{
            location first = this.locations.get(0);
            location second = this.locations.get(1);
            node ans = this.clone();
            int tempFirst = this.state[first.getY()][first.getX()];
            ans.state[first.getY()][first.getX()] = this.state[first.getY()][first.getX()+1];
            ans.state[first.getY()][first.getX()+1] = tempFirst;

            int tempSecond = this.state[second.getY()][second.getX()];
            ans.state[second.getY()][second.getX()] = this.state[second.getY()][second.getX()+1];
            ans.state[second.getY()][second.getX()+1] = tempSecond;

            //set new location:
            ans.locations.get(0).setX(first.getX()+1);
            ans.locations.get(1).setX(second.getX()+1);

            ans.prevState = this;
            ans.howIGotHere = "" + ans.state[first.getY()][first.getX()] + "&" + ans.state[second.getY()][second.getX()] + "L";
            ans.setCostToHere(this.getCostToHere()+6);
            if(ans.equals(this.prevState)) {
                return null;
            }
            else{
                return ans;
            }
        }
    }
    /**
     * Move the two jointed blank tiles 1 step left. If possible.
     * cost-> 6
     * @return the state which we move to.
     */
    public node twoLeft(){
        if(this.locations.get(0).getX() == 0){
            return null;
        }
        else{
            location first = this.locations.get(0);
            location second = this.locations.get(1);
            node ans = this.clone();
            int tempFirst = this.state[first.getY()][first.getX()];
            ans.state[first.getY()][first.getX()] = this.state[first.getY()][first.getX()-1];
            ans.state[first.getY()][first.getX()-1] = tempFirst;

            int tempSecond = this.state[second.getY()][second.getX()];
            ans.state[second.getY()][second.getX()] = this.state[second.getY()][second.getX()-1];
            ans.state[second.getY()][second.getX()-1] = tempSecond;

            //set new location:
            ans.locations.get(0).setX(first.getX()-1);
            ans.locations.get(1).setX(second.getX()-1);

            ans.prevState = this;
            ans.howIGotHere = "" + ans.state[first.getY()][first.getX()] + "&" + ans.state[second.getY()][second.getX()] + "R";
            ans.setCostToHere(this.getCostToHere()+6);
            if(ans.equals(this.prevState)) {
                return null;
            }
            else{
                return ans;
            }
        }
    }
    /**
     * Move the two jointed blank tiles 1 step up. If possible.
     * cost-> 7
     * @return the state which we move to.
     */
    public node twoUp(){
        if(this.locations.get(0).getY() == 0){
            return null;
        }
        else{
            location first = this.locations.get(0);
            location second = this.locations.get(1);
            node ans = this.clone();
            int tempFirst = this.state[first.getY()][first.getX()];
            ans.state[first.getY()][first.getX()] = this.state[first.getY()-1][first.getX()];
            ans.state[first.getY()-1][first.getX()] = tempFirst;

            int tempSecond = this.state[second.getY()][second.getX()];
            ans.state[second.getY()][second.getX()] = this.state[second.getY()-1][second.getX()];
            ans.state[second.getY()-1][second.getX()] = tempSecond;

            //set new location:
            ans.locations.get(0).setY(first.getY()-1);
            ans.locations.get(1).setY(first.getY()-1);

            ans.prevState = this;
            ans.howIGotHere = "" + ans.state[first.getY()][first.getX()] + "&" + ans.state[second.getY()][second.getX()] + "D";
            ans.setCostToHere(this.getCostToHere()+7);
            if(ans.equals(this.prevState)) {
                return null;
            }
            else{
                return ans;
            }
        }

    }
    /**
     * Move the two jointed blank tiles 1 step down. If possible.
     * cost-> 7
     * @return the state which we move to.
     */
    public node twoDown(){
        if(this.locations.get(0).getY() == this.state.length-1){
            return null;
        }
        else{
            location first = this.locations.get(0);
            location second = this.locations.get(1);
            node ans = this.clone();
            int tempFirst = this.state[first.getY()][first.getX()];
            ans.state[first.getY()][first.getX()] = this.state[first.getY()+1][first.getX()];
            ans.state[first.getY()+1][first.getX()] = tempFirst;

            int tempSecond = this.state[second.getY()][second.getX()];
            ans.state[second.getY()][second.getX()] = this.state[second.getY()+1][second.getX()];
            ans.state[second.getY()+1][second.getX()] = tempSecond;

            //set new location:
            ans.locations.get(0).setY(first.getY()+1);
            ans.locations.get(1).setY(first.getY()+1);

            ans.prevState = this;
            if(first.compareTo(second) > 0){
                location temp = second;
                second = first;
                first = temp;
            }
            ans.howIGotHere = "" + ans.state[first.getY()][first.getX()] + "&" + ans.state[second.getY()][second.getX()] + "U";
            if(ans.howIGotHere.equals("5&2U")){
                System.out.println("");
            }
            ans.setCostToHere(this.getCostToHere()+7);
            if(ans.equals(this.prevState)) {
                return null;
            }
            else{
                return ans;
            }
        }
    }

    @Override
    public String toString() {
        String ans = "{";
        for(int i=0; i<this.getState().length; i++){
            ans += "{";
            for(int j=0; j<this.getState()[i].length; j++){
                ans += this.getState()[i][j];
            }
            ans+="}\n";
        }
        ans += "}\n";
        return ans;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if (!(obj instanceof node)){
            return false;
        }

        for(int i=0; i<this.getState().length; i++){
            for(int j=0; j<this.getState()[i].length; j++){
                if(this.state[i][j] != ((node) obj).getState()[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Makes a deep copy of this node.
     * @return deep copy of this node.
     */
    public node clone(){
        int[][] ans = new int[this.state.length][this.state[0].length];
        for(int i = 0; i<this.state.length; i++){
            for(int j =0; j<this.state[i].length; j++){
                ans[i][j] = this.state[i][j];
            }
        }
        node clone = new node(ans, this);
        return clone;
    }

    @Override
    public int compareTo(node o) {
        if(o.f < this.f){
            return 1;
        }
        else if(o.f > this.f){
            return -1;
        }
        else{
            if(o.getId() < this.id){
                return -1;
            }
            else if(o.getId() > this.id){
                return 1;
            }
            else{
                return 0;
            }
        }
    }

    /**
     * heuristic function which sums all misplaced tiles steps to get to goal state.
     * @return the total sum calculated as described.
     */
    private int manhattan(node goal){
        int normalCost = 0;
        int doubleMoveCost = 0;
        int isJoint = this.isJoint();
        double cost =0;
        int informer = -1;


        // cam move horizontally.
        if(isJoint == -1){
            //If it can move only right.
            if(this.locations.get(0).getX() == 0){
                //If we can move 2 goal state neighbors.
                if(this.getState()[this.locations.get(1).getY()][1] - this.getState()[this.locations.get(0).getY()][1] == this.getState()[0].length){
                    //Start calculate the Manhattan distance.
                    for (int i = 0; i < this.getState().length; i++) {
                        for (int j = 0; j < this.getState()[i].length; j++) {
                            //If a misplaced tile has found
                            if (this.getState()[i][j] != (i * this.getState()[i].length + (j + 1)) && this.getState()[i][j] != -1) {
                                //If this misplaced tile is next to a blank tile.
                                if(j == 1 && (i == this.locations.get(0).getY() || i == this.locations.get(1).getY())){
                                    cost += (5 * Math.abs(i - getRow(this.getState()[i][j], goal))) + (3*Math.abs(j - getCol(this.getState()[i][j], goal)));
                                }
                                //If it is misplaced somewhere on the board.
                                else {
                                    if(informer == -1) {
                                        informer = nextTo(i, j, goal);
                                        if (informer != -1) {
                                            if (informer == i + 1) {
                                                cost += (3 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                            else if (informer == j + 1) {
                                                cost += (3.5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                        }
                                        else {
                                            cost += (5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                        }
                                    }
                                    else {//release informer.
                                        informer = -1;
                                    }
                                }
                            }
                        }
                    }
                    doubleMoveCost = (int)cost;
                    return doubleMoveCost;
                }
            }
            // If we can move only left.
            else if(this.locations.get(0).getX() == this.getState()[0].length-1){
                //If we can move 2 goal state neighbors.
                if(this.getState()[this.locations.get(1).getY()][this.locations.get(1).getX()-1] - this.getState()[this.locations.get(0).getY()][this.locations.get(0).getX()-1] == this.getState()[0].length){
                    //Start calculate the Manhattan distance.
                    for (int i = 0; i < this.getState().length; i++) {
                        for (int j = 0; j < this.getState()[i].length; j++) {
                            if (this.getState()[i][j] != (i * this.getState()[i].length + (j + 1)) && this.getState()[i][j] != -1) {
                                if(j == locations.get(0).getX()-1 && (i == this.locations.get(0).getY() || i == this.locations.get(1).getY())){
                                    cost += (3 * Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal)));
                                }
                                else {
                                    if(informer == -1) {
                                        informer = nextTo(i, j, goal);
                                        if (informer != -1) {
                                            if (informer == i + 1) {
                                                cost += (3 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                            else if (informer == j + 1) {
                                                cost += (3.5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                        }
                                        else {
                                            cost += (5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                        }
                                    }
                                    else {//release informer.
                                        informer = -1;
                                    }
                                }
                            }
                        }
                    }
                    doubleMoveCost = (int)cost;
                    return doubleMoveCost;
                }
            }
            //If we can move left and right.
            else{
                //If we can move 2 goal state neighbors.
                if(this.getState()[this.locations.get(1).getY()][this.locations.get(1).getX()-1] - this.getState()[this.locations.get(0).getY()][this.locations.get(0).getX()-1] == this.getState()[0].length ||
                        this.getState()[this.locations.get(1).getY()][this.locations.get(1).getX()+1] - this.getState()[this.locations.get(0).getY()][this.locations.get(0).getX()+1] == this.getState()[0].length){
                    //Start calculate the Manhattan distance.
                    for (int i = 0; i < this.getState().length; i++) {
                        for (int j = 0; j < this.getState()[i].length; j++) {
                            if (this.getState()[i][j] != (i * this.getState()[i].length + (j + 1)) && this.getState()[i][j] != -1) {
                                if(j == locations.get(0).getX()-1 && (i == this.locations.get(0).getY() || i == this.locations.get(1).getY()) ||
                                        j == locations.get(0).getX()+1 && (i == this.locations.get(0).getY() || i == this.locations.get(1).getY())){
                                    cost += (3 * Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal)));
                                }
                                else {
                                    if(informer == -1) {
                                        informer = nextTo(i, j, goal);
                                        if (informer != -1) {
                                            if (informer == i + 1) {
                                                cost += (3 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                            else if (informer == j + 1) {
                                                cost += (3.5 *(Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                        }
                                        else {
                                            cost += (5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                        }
                                    }
                                    else {//release informer.
                                        informer = -1;
                                    }
                                }
                            }
                        }
                    }
                    doubleMoveCost = (int)cost;
                    return doubleMoveCost;
                }
            }
        }
        //can move vertically
        else if(isJoint == 1){
            //If it can move only down.
            if(this.locations.get(0).getY() == 0){
                //If we can move 2 goal state neighbors.
                if(this.getState()[this.locations.get(0).getY()+1][this.locations.get(1).getX()] - this.getState()[this.locations.get(1).getY()+1][this.locations.get(0).getX()] == 1){
                    //Start calculate the Manhattan distance.
                    for (int i = 0; i < this.getState().length; i++) {
                        for (int j = 0; j < this.getState()[i].length; j++) {
                            if (this.getState()[i][j] != (i * this.getState()[i].length + (j + 1)) && this.getState()[i][j] != -1) {
                                if(i == 1 && (j == this.locations.get(0).getX() || j == this.locations.get(1).getX())){
                                    cost += (3.5 * Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal)));
                                }
                                else {
                                    if(informer == -1) {
                                        informer = nextTo(i, j, goal);
                                        if (informer != -1) {
                                            if (informer == i + 1) {
                                                cost += (3 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                            else if (informer == j + 1) {
                                                cost += (3.5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                        }
                                        else {
                                            cost += (5 *(Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                        }
                                    }
                                    else {//release informer.
                                        informer = -1;
                                    }
                                }
                            }
                        }
                    }
                    doubleMoveCost = (int)cost;
                    return doubleMoveCost;
                }
            }
            //If it can move only up.
            else if(this.locations.get(0).getY() == this.getState().length-1){
                //If we can move 2 goal state neighbors.
                if(this.getState()[this.locations.get(1).getY()-1][this.locations.get(1).getX()] - this.getState()[this.locations.get(0).getY()-1][this.locations.get(0).getX()] == 1){
                    //Start calculate the Manhattan distance.
                    for (int i = 0; i < this.getState().length; i++) {
                        for (int j = 0; j < this.getState()[i].length; j++) {
                            if (this.getState()[i][j] != (i * this.getState()[i].length + (j + 1)) && this.getState()[i][j] != -1) {
                                if(i == this.locations.get(0).getY()-1 && (j == this.locations.get(0).getX() || j == this.locations.get(1).getX())){
                                    cost += (3.5 * Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal)));
                                }
                                else {
                                    if(informer == -1) {
                                        informer = nextTo(i, j, goal);
                                        if (informer != -1) {
                                            if (informer == i + 1) {
                                                cost += (3 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                            else if (informer == j + 1) {
                                                cost += (3.5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                        }
                                        else {
                                            cost += (5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                        }
                                    }
                                    else {//release informer.
                                        informer = -1;
                                    }
                                }
                            }
                        }
                    }
                    doubleMoveCost = (int)cost;
                    return doubleMoveCost;
                }
            }
            //If it can move up and down.
            else{
                //If we can move 2 goal state neighbors.
                if(this.getState()[this.locations.get(1).getY()-1][this.locations.get(1).getX()] - this.getState()[this.locations.get(0).getY()-1][this.locations.get(0).getX()] == 1 ||
                        this.getState()[this.locations.get(1).getY()+1][this.locations.get(1).getX()] - this.getState()[this.locations.get(0).getY()+1][this.locations.get(0).getX()] == 1){
                    //Start calculate the Manhattan distance.
                    for (int i = 0; i < this.getState().length; i++) {
                        for (int j = 0; j < this.getState()[i].length; j++) {
                            if (this.getState()[i][j] != (i * this.getState()[i].length + (j + 1)) && this.getState()[i][j] != -1) {
                                if(i == this.locations.get(0).getY()-1 && (j == this.locations.get(0).getX() || j == this.locations.get(1).getX()) ||
                                        i == this.locations.get(0).getY()+1 && (j == this.locations.get(0).getX() || j == this.locations.get(1).getX())){
                                    cost += (3.5 * Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal)));
                                }
                                else {
                                    if(informer == -1) {
                                        informer = nextTo(i, j, goal);
                                        if (informer != -1) {
                                            if (informer == i + 1) {
                                                cost += (3 *(Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                            else if (informer == j + 1) {
                                                cost += (3.5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                            }
                                        }
                                        else {
                                            cost += (5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                                        }
                                    }
                                    else {//release informer.
                                        informer = -1;
                                    }
                                }
                            }
                        }
                    }
                    doubleMoveCost = (int)cost;
                    return doubleMoveCost;
                }
            }
        }
        // If they are not jointed, or there is only one empty tile.
        for (int i = 0; i < this.getState().length; i++) {
            for (int j = 0; j < this.getState()[i].length; j++) {
                if (this.getState()[i][j] != (i * this.getState()[i].length + (j + 1)) && this.getState()[i][j] != -1) {
                    if(informer == -1) {
                        informer = nextTo(i, j, goal);
                        if (informer != -1) {
                            if (informer == i + 1) {
                                cost += ((6 * (Math.abs(i - getRow(this.getState()[i][j], goal)))) + (5*Math.abs(j - getCol(this.getState()[i][j], goal))));
                            }
                            else if (informer == j + 1) {
                                cost += ((7 * (Math.abs(i - getRow(this.getState()[i][j], goal)))) + (5*Math.abs(j - getCol(this.getState()[i][j], goal))));
                            }
                        }
                        else {
                            cost += (5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                        }
                    }
                    else {//release informer.
                        informer = -1;
                    }
                }
            }
        }
        normalCost = (int)cost;
        return normalCost;
    }

    private int manhattan1(node goal) {
        int cost = 0;
        int tempCost = 0;
        int informer = -1;
        for (int i = 0; i < this.getState().length; i++) {
            for (int j = 0; j < this.getState()[i].length; j++) {
                if (this.getState()[i][j] != (i * this.getState()[i].length + (j + 1)) && this.getState()[i][j] != -1) {
                    if (informer == -1) {
                        informer = nextTo(i, j, goal);
                        if (informer != -1) {
                            if (informer == i + 1) {
                                cost += ((6 * (Math.abs(i - getRow(this.getState()[i][j], goal)))) + (5 * Math.abs(j - getCol(this.getState()[i][j], goal))));
                            } else if (informer == j + 1) {
                                cost += ((7 * (Math.abs(i - getRow(this.getState()[i][j], goal)))) + (5 * Math.abs(j - getCol(this.getState()[i][j], goal))));
                            }
                        } else {
                            cost += (5 * (Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal))));
                        }
                    } else {//release informer.
                        informer = -1;
                    }
                }
            }
        }
        return cost;
    }

    private int manhattan2(node goal){
        int cost = 0;
        int tempCost = 0;
        for (int i = 0; i < this.getState().length; i++) {
            for (int j = 0; j < this.getState()[i].length; j++) {
                if (this.getState()[i][j] != goal.getState()[i][j] && this.getState()[i][j] != -1) {
                    //If the one right to you is also right to you in the goal state.
                    if(j < this.getState()[i].length-1 &&
                            this.getState()[i][j+1] == goal.getState()[getRow(this.getState()[i][j+1], goal)][getCol(this.getState()[i][j+1], goal)]){
                        tempCost = 7*(Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal)));
                        cost += tempCost;
                        j++;
                    }
                    //If the one beneath me is is also beneath me in the goal state.
                    else if(i<this.getState().length-1 &&
                            this.getState()[i+1][j] == goal.getState()[getRow(this.getState()[i+1][j], goal)][getCol(this.getState()[i+1][j], goal)]){
                        tempCost = 6*(Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal)));
                        cost += tempCost;
                    }
                    else{
                        tempCost = 3*(Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal)));
                        cost += tempCost;
                    }
                }
            }
        }
        return cost;
    }

    private int normalManhattan(node goal){
        int cost = 0;
        for(int i = 0; i<this.getState().length; i++) {
            for(int j = 0; j< this.getState()[i].length; j++){
                if(this.getState()[i][j] != (i*this.getState()[i].length + (j+1)) && this.getState()[i][j] != -1){
                    cost += Math.abs(i - getRow(this.getState()[i][j], goal)) + Math.abs(j - getCol(this.getState()[i][j], goal));
                }
            }
        }
        return cost;
    }

    /**
     * helper to get the expected row of a value at the goal state.
     * @param value the value we search.
     * @return the expected row.
     */
    public int getRow(int value, node goal){
        for(int i = 0; i<goal.getState().length; i++) {
            for(int j = 0; j< goal.getState()[i].length; j++){
                if(goal.getState()[i][j] == value ) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * helper to get the expected column of a value at the goal state.
     * @param value the value we search.
     * @return the expected column.
     */
    public int getCol(int value, node goal){
        for(int i = 0; i<goal.getState().length; i++) {
            for(int j = 0; j< goal.getState()[i].length; j++){
                if(goal.getState()[i][j] == value ) {
                    return j;
                }
            }
        }
        return -1;
    }

    /**
     * helper function
     */
    int nextTo(int i, int j, node goal){
        int ans = -1;
        int row = getRow(this.getState()[i][j], goal);
        int col = getCol(this.getState()[i][j], goal);
        //if the current state neighbor is at some edge
        if(row == this.getState().length-1 || col == this.getState()[0].length-1){
            return -1;
        }
        //If the one right to you is also right to you in the goal state.
        if (j < this.getState()[i].length - 1 && this.getState()[i][j + 1] != -1 &&
                this.getState()[i][j + 1] == goal.getState()[row][col + 1]) {
            ans = j + 1;
        }
        //If the one beneath me is is also beneath me in the goal state.
        else if(i<this.getState().length-1 && this.getState()[i+1][j] != -1 &&
                this.getState()[i+1][j] == goal.getState()[row+1][col]){
            ans = i+1;
        }

        return ans;
    }
}
