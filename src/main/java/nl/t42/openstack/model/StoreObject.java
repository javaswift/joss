package nl.t42.openstack.model;

public class StoreObject {

    private String name;

    public StoreObject(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getName();
    }
}
