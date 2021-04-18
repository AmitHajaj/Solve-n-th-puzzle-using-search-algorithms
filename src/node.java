import java.util.List;

public class node {
    private int id;
    private List<int[]> state;
    private int prevState;
    private boolean isTwo;
    private int firstHorizontal;
    private int firstVertical;
    private int secondHorizontal;
    private int secondVertical;




    public node(List<int[]> state, int prevID) {
        this.id = id;
        this.state = state;
        this.prevState = prevID;
        this.isTwo = f;
        int cnt = 0;

        for(int i=0; i< state.size(); i++){
            for(int j=0; j<state.get(i).length; j++){
                if(state.get(i)[j] != -1){
                    continue;
                }
                else{
                    if(cnt == 0){
                        firstHorizontal = i;
                        firstVertical = j;
                        cnt++;
                    }
                    else{
                        secondHorizontal = i;
                        secondVertical = j;
                        cnt++;
                        this.isTwo = true;
                    }
                }
            }
        }
        if (cnt == 1){
            secondHorizontal = -1;
            secondVertical = -1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<int[]> getState() {
        return state;
    }

    public void setState(List<int[]> state) {
        this.state = state;
    }

    public int getPrevState() {
        return prevState;
    }

    public void setPrevState(int prevState) {
        this.prevState = prevState;
    }

    public int manhattenDistance(){

        return 0;
    }

    /**
     * If possible ot return the state after applying the down operator.
     * @return node
     */
    public node down(){
        node down = new node(this.state, this.id);
        boolean flag = true;
        for(int i=0; i<down.getState().size()-1; i++){
            for(int j = 0; j<down.getState().get(i).length-1; j++){
                if(this.getState().get(i)[j] != -1){
                    continue;
                }
                else{
                    if(i == down.getState().size()-1){
                        return null;
                    }
                    else{
                        int temp = down.getState().get(i)[j];
                        down.getState().get(i)[j] = down.getState().get(i+1)[j];
                        down.getState().get(i+1)[j] = temp;
                    }
                }
            }
        }
        return down;
    }

    public node up(){

    }

    public node left(){

    }

    public node right(){

    }
}
