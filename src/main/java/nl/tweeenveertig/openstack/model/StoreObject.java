package nl.tweeenveertig.openstack.model;

public class StoreObject implements Comparable {

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

    public int hashcode() {
        return getName().hashCode();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean equals(Object o) {
        return o instanceof StoreObject && getName().equals(((StoreObject) o).getName());
    }

    @SuppressWarnings("ConstantConditions")
    public int compareTo(Object o) {
        return o instanceof StoreObject ? getName().compareTo(((StoreObject) o).getName()) : -1;
    }
}
