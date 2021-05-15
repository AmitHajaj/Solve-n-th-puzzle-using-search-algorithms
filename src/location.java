public class location implements Comparable<location> {
    private int x;
    private int y;

    public location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public int compareTo(location o) {
        //same depth
        if (this.x == o.x) {
            if (o.y < this.y) {
                return 1;
            }
            else if (o.y > this.y) {
                return -1;
            }
            else {
                return 0;
            }
        }
        //same length
        if (this.y == o.y) {
            if (o.x < this.x) {
                return 1;
            }
            else {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj){
        location o = (location) obj;
        if(o.getX() != this.x){
            return false;
        }
        else if(o.getY() != this.y){
            return false;
        }
        else{
            return true;
        }
    }
}
