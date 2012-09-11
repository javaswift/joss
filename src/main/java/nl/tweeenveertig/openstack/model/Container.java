package nl.tweeenveertig.openstack.model;

public class Container implements Comparable<Container> {

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

    public int hashCode() {
        return getName().hashCode();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean equals(Object o) {
        return o instanceof Container && getName().equals(((Container) o).getName());
    }

    @SuppressWarnings("ConstantConditions")
    public int compareTo(Container o) {
        return getName().compareTo(o.getName());
    }

}
