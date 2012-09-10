package nl.tweeenveertig.openstack.model;

public class Container implements Comparable {

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

    public int hashcode() {
        return getName().hashCode();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean equals(Object o) {
        return o instanceof Container && getName().equals(((Container) o).getName());
    }

    @SuppressWarnings("ConstantConditions")
    public int compareTo(Object o) {
        return o instanceof Container ? getName().compareTo(((Container) o).getName()) : -1;
    }

}
