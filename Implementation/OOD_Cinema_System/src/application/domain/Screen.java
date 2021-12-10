package application.domain;

public class Screen {

    private String name;
    private int capacity;

    public Screen(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    // get methods
    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

}
