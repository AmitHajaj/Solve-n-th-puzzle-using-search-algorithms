import java.util.ArrayList;
import java.util.List;

public class node {
    private int id;
    private int[][] state;
    private int prevState;
    private String howIGotHere;
    location [] locations;
    private static int stateCounter = 0; //Keeps track on the states that created.




    public node(int[][] state, int prevID) {
        this.id = id;
        this.state = state;
        this.prevState = prevID;
        this.howIGotHere = "";
        stateCounter++;
        int cnt = 0;
        for(int i=0; i<state.length; i++){
            for(int j=0; j<state[i].length; j++){
                if(state[i][j] == -1){
                    if(cnt == 0) {
                        location first = new location(j, i);
                        cnt++;
                    }
                    else if(cnt == 1){
                        location second = new location(j, i);
                    }
                }
            }
        }
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

    public int getPrevState() {
        return prevState;
    }

    public void setPrevState(int prevState) {
        this.prevState = prevState;
    }

    /**
     * In case there is two blnak tiles on the puzzle, this method determine if they are joint.
     * @return 1 - joint horizontally
     *         -1 - joint vertically
     *         0 - not jointed at all or there is only one blank tile.
     */
    public int isJoint(){
        if(this.locations.length == 1){
            return 0; //Only one blank tile.
        }
        else{
            for(int i = 0; i<this.locations.length; i++){
                if(locations[i].getX() == locations[i+1].getX()){
                    if(Math.abs(locations[i].getY() - locations[i+1].getY()) == 1){
                        return 1; //Horizontal jointed.
                    }
                    else{
                        return 0;
                    }
                }
                else if(locations[i].getY() == locations[i+1].getY()){
                    if(Math.abs(locations[i].getX() - locations[i+1].getX()) == 1){
                        return -1; //Vertically jointed.
                    }
                    else{
                        return 0;
                    }
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
        for(location l : this.locations) {
            temp.add(this.down(l));
            temp.add(this.up(l));
            temp.add(this.right(l));
            temp.add(this.left(l));
        }

        if(isJoint == 1){
            temp.add(this.twoDown());
            temp.add(this.twoUp());
        }
        else if(isJoint == -1){
            temp.add(this.twoRight());
            temp.add(this.twoLeft());
        }

        for(node n : temp){
            if(n != null){
                ans.add(n);
            }
        }
        return ans;
    }

//    public int manhattenDistance(){
//
//        return 0;
//    }

    /**
     * Move the blank tile 1 step down. If possible.
     * cost-> 5
     * @param l- the location of the blank tile.
     * @return the state which we move to.
     */
    public node down(location l){
        if(l.getY() == this.state.length-1){
            return null;
        }
        else{
            node ans = this.clone();
            int temp = this.state[l.getY()][l.getX()];
            ans.state[l.getY()][l.getX()] = this.state[l.getY()+1][l.getX()];
            ans.state[l.getY()+1][l.getX()] = temp;

            ans.prevState = this.id;
            ans.howIGotHere = "" + ans.state[l.getY()][l.getX()] + "D";
            return ans;
        }
    }
    /**
     * Move the blank tile 1 step up. If possible.
     * cost-> 5
     * @param l- the location of the blank tile.
     * @return the state which we move to.
     */
    public node up(location l){
        if(l.getY() == 0){
            return null;
        }
        else{
            node ans = this.clone();
            int temp = this.state[l.getY()][l.getX()];
            ans.state[l.getY()][l.getX()] = this.state[l.getY()-1][l.getX()];
            ans.state[l.getY()-1][l.getX()] = temp;

            ans.prevState = this.id;
            ans.howIGotHere = "" + ans.state[l.getY()][l.getX()] + "U";
            return ans;
        }
    }
    /**
     * Move the blank tile 1 step left. If possible.
     * cost-> 5
     * @param l- the location of the blank tile.
     * @return the state which we move to.
     */
    public node left(location l){
        if(l.getX() == 0){
            return null;
        }
        else{
            node ans = this.clone();
            int temp = this.state[l.getY()][l.getX()];
            ans.state[l.getY()][l.getX()] = this.state[l.getY()][l.getX()-1];
            ans.state[l.getY()][l.getX()-1] = temp;

            ans.prevState = this.id;
            ans.howIGotHere = "" + ans.state[l.getY()][l.getX()] + "L";
            return ans;
        }
    }
    /**
     * Move the blank tile 1 step right. If possible.
     * cost-> 5
     * @param l- the location of the blank tile.
     * @return the state which we move to.
     */
    public node right(location l){
        if(l.getX() == this.state[0].length-1){
            return null;
        }
        else{
            node ans = this.clone();
            int temp = this.state[l.getY()][l.getX()];
            ans.state[l.getY()][l.getX()] = this.state[l.getY()][l.getX()+1];
            ans.state[l.getY()][l.getX()+1] = temp;

            ans.prevState = this.id;
            ans.howIGotHere = "" + ans.state[l.getY()][l.getX()] + "R";
            return ans;
        }
    }
    /**
     * Move the two jointed blank tiles 1 step right. If possible.
     * cost-> 6
     * @return the state which we move to.
     */
    public node twoRight(){
        if(this.locations[0].getX() == this.state[0].length-1){
            return null;
        }
        else{
            location first = this.locations[0];
            location second = this.locations[1];
            node ans = this.clone();
            int tempFirst = this.state[first.getY()][first.getX()];
            ans.state[first.getY()][first.getX()] = this.state[first.getY()][first.getX()+1];
            ans.state[first.getY()][first.getX()+1] = tempFirst;

            int tempSecond = this.state[second.getY()][second.getX()];
            ans.state[second.getY()][second.getX()] = this.state[second.getY()][second.getX()+1];
            ans.state[second.getY()][second.getX()+1] = tempSecond;

            ans.prevState = this.id;
            ans.howIGotHere = "" + ans.state[first.getY()][first.getX()] + "&" + ans.state[second.getY()][second.getX()] + "R";
            return ans;
        }
    }
    /**
     * Move the two jointed blank tiles 1 step left. If possible.
     * cost-> 6
     * @return the state which we move to.
     */
    public node twoLeft(){
        if(this.locations[0].getX() == 0){
            return null;
        }
        else{
            location first = this.locations[0];
            location second = this.locations[1];
            node ans = this.clone();
            int tempFirst = this.state[first.getY()][first.getX()];
            ans.state[first.getY()][first.getX()] = this.state[first.getY()][first.getX()-1];
            ans.state[first.getY()][first.getX()-1] = tempFirst;

            int tempSecond = this.state[second.getY()][second.getX()];
            ans.state[second.getY()][second.getX()] = this.state[second.getY()][second.getX()-1];
            ans.state[second.getY()][second.getX()-1] = tempSecond;

            ans.prevState = this.id;
            ans.howIGotHere = "" + ans.state[first.getY()][first.getX()] + "&" + ans.state[second.getY()][second.getX()] + "L";
            return ans;
        }
    }
    /**
     * Move the two jointed blank tiles 1 step up. If possible.
     * cost-> 7
     * @return the state which we move to.
     */
    public node twoUp(){
        if(this.locations[0].getY() == 0){
            return null;
        }
        else{
            location first = this.locations[0];
            location second = this.locations[1];
            node ans = this.clone();
            int tempFirst = this.state[first.getY()][first.getX()];
            ans.state[first.getY()][first.getX()] = this.state[first.getY()-1][first.getX()];
            ans.state[first.getY()-1][first.getX()] = tempFirst;

            int tempSecond = this.state[second.getY()][second.getX()];
            ans.state[second.getY()][second.getX()] = this.state[second.getY()-1][second.getX()];
            ans.state[second.getY()-1][second.getX()] = tempSecond;

            ans.prevState = this.id;
            ans.howIGotHere = "" + ans.state[first.getY()][first.getX()] + "&" + ans.state[second.getY()][second.getX()] + "U";
            return ans;
        }

    }
    /**
     * Move the two jointed blank tiles 1 step down. If possible.
     * cost-> 7
     * @return the state which we move to.
     */
    public node twoDown(){
        if(this.locations[0].getY() == this.state.length-1){
            return null;
        }
        else{
            location first = this.locations[0];
            location second = this.locations[1];
            node ans = this.clone();
            int tempFirst = this.state[first.getY()][first.getX()];
            ans.state[first.getY()][first.getX()] = this.state[first.getY()+1][first.getX()];
            ans.state[first.getY()+1][first.getX()] = tempFirst;

            int tempSecond = this.state[second.getY()][second.getX()];
            ans.state[second.getY()][second.getX()] = this.state[second.getY()+1][second.getX()];
            ans.state[second.getY()+1][second.getX()] = tempSecond;

            ans.prevState = this.id;
            ans.howIGotHere = "" + ans.state[first.getY()][first.getX()] + "&" + ans.state[second.getY()][second.getX()] + "D";
            return ans;
        }
    }

    /**
     * Makes a deep copy of this node.
     * @return deep copy of this node.
     */
    public node clone(){
        int[][] ans = new int[this.state.length][this.state[0].length];
        for(int i = 0; i<this.state[i].length; i++){
            for(int j =0; j<this.state[i].length; j++){
                ans[i][j] = this.state[i][j];
            }
        }
        node clone = new node(ans, this.id);
        return clone;
    }
}
