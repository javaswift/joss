package nl.t42.openstack.model;

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

    public int compareTo(Object o) {
        if (!(o instanceof StoreObject)) {
            return 0;
        }
        StoreObject compareObject = (StoreObject)o;
        return getName().compareTo(compareObject.getName());
    }
}
