public class Player {
    protected String name;
    double depth;
    boolean deadFlag = false;

    public Player(String name) {
        this.name = name;
    }

    public boolean isDead() {
        return deadFlag;
    }
    public String getName() {
        return name;
    }
    public void kill() {
        deadFlag = true;
    }
}
