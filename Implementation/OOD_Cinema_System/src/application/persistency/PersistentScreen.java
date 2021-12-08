package application.persistency;

import application.domain.Screen;

public class PersistentScreen extends Screen {
    private int oid;

    public PersistentScreen(int oid, String name, int capacity) {
        super(name, capacity);
        this.oid = oid;
    }

    int getOid() {
        return oid;
    }

    @Override
    public String toString() {
        return "PersistentScreen{" +
                "oid=" + oid +
                ", name=" + getName() +
                ", capacity=" + getCapacity() +
                '}';
    }
}
