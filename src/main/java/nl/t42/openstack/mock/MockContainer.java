package nl.t42.openstack.mock;

import nl.t42.openstack.model.ContainerInformation;
import nl.t42.openstack.model.StoreObject;

import java.util.Map;
import java.util.TreeMap;

public class MockContainer {

    private Map<StoreObject, MockObject> objects = new TreeMap<StoreObject, MockObject>();

    private Map<String, Object> metadata = new TreeMap<String, Object>();

    private boolean publicContainer = false;

    public int getNumberOfObjects() {
        return objects.size();
    }

    public MockObject getObject(StoreObject object) {
        return objects.get(object);
    }

    public void makeContainerPublic() {
        this.publicContainer = true;
    }

    public void makeContainerPrivate() {
        this.publicContainer = false;
    }

    public void setInfo(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public ContainerInformation getInfo() {
        ContainerInformation info = new ContainerInformation();
        for (String metadataKey : metadata.keySet()) {
            info.addMetadata(metadataKey, metadata.get(metadataKey).toString());
        }
        return info;
    }

    public StoreObject[] listObjects() {
        return objects.keySet().toArray(new StoreObject[objects.size()]);
    }
}
