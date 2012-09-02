package nl.t42.openstack.model;

public class Container {

    private String name;

    public Container(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getName();
    }
}
