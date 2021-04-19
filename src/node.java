import java.util.List;

public class node {
    private int id;
    private List<int[]> state;
    private int prevState;
    location [] locations;




    public node(List<int[]> state, int prevID) {
        this.id = id;
        this.state = state;
        this.prevState = prevID;
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


    public node down(){
    }

    public node up(){

    }

    public node left(){

    }

    public node right(){

    }
}
