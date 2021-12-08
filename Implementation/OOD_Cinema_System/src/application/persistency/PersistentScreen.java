package application.persistency;

import application.domain.Screen;

public class PersistentScreen extends Screen {
    private int oid;

    public PersistentScreen(int oid, String name, int capacity) {
        super(name, capacity);
        this.oid = oid;
    }

    public int getOid() {
        return oid;
    }
}
