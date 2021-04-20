import java.util.HashMap;

public class puzzle {
    private node goalState;
    private node currentState;
    private HashMap<String, Integer> priceTable;


    public puzzle(node startState, node goalState, HashMap<String, Integer> priceTable){
        this.currentState = startState;
        this.goalState = goalState;
        this.priceTable = priceTable;
    }

    /**
     * heuristic function which sums all misplaced tiles steps to get to goal state.
     * @return the total sum calculated as described.
     */
    public int manhattan(){
        int cost = 0;
        for(int i = 0; i<this.currentState.getState().length; i++) {
            for(int j = 0; j< this.currentState.getState()[i].length; j++){
                if(this.currentState.getState()[i][j] != (i*this.currentState.getState()[i].length + (j+1)) ){
                    cost += Math.abs(i - getRow(this.currentState.getState()[i][j])) + Math.abs(j - getCol(this.currentState.getState()[i][j]));
                }
            }
        }
        return cost;
    }

    /**
     * heuristic function which sums the number of misplaced tiles.
     * @return the total sum calculated as described.
     */
    public int hamming(){
        int cost = 0;
        for(int i = 0; i<this.currentState.getState().length; i++) {
            for(int j = 0; j< this.currentState.getState()[i].length; j++){
                if(this.currentState.getState()[i][j] != (i*this.currentState.getState()[i].length + (j+1)) ){
                    cost++;
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
    public int getRow(int value){
        for(int i = 0; i<this.goalState.getState().length; i++) {
            for(int j = 0; j< this.goalState.getState()[i].length; j++){
                if(this.goalState.getState()[i][j] == value ) {
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
    public int getCol(int value){
        for(int i = 0; i<this.goalState.getState().length; i++) {
            for(int j = 0; j< this.goalState.getState()[i].length; j++){
                if(this.goalState.getState()[i][j] == value ) {
                    return j;
                }
            }
        }
        return -1;
    }



}
