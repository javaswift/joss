package nl.t42.openstack.mock;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.ContainerInformation;
import nl.t42.openstack.model.ObjectInformation;
import nl.t42.openstack.model.StoreObject;
import org.apache.http.HttpStatus;

import java.util.Map;
import java.util.TreeMap;

public class MockContainer extends AbstractMock<ContainerInformation>{

    private Map<StoreObject, MockObject> objects = new TreeMap<StoreObject, MockObject>();

    private boolean publicContainer = false;

    public MockObject getObject(StoreObject object) {
        MockObject foundObject = objects.get(object);
        if (foundObject == null) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
        }
        return foundObject;
    }

    public int getNumberOfObjects() {
        return objects.size();
    }

    public void makeContainerPublic() {
        this.publicContainer = true;
    }

    public void makeContainerPrivate() {
        this.publicContainer = false;
    }

    @Override
    protected void appendInformation(ContainerInformation info) {
        info.setPublicContainer(this.publicContainer);
        int numberOfObjects = 0;
        long numberOfBytes = 0;
        for (MockObject object : objects.values()) {
            numberOfObjects++;
            numberOfBytes += object.getInfo().getContentLength();
        }
        info.setObjectCount(numberOfObjects);
        info.setBytesUsed(numberOfBytes);
    }

    @Override
    protected ContainerInformation createInformationContainer() {
        return new ContainerInformation();
    }

    public StoreObject[] listObjects() {
        return objects.keySet().toArray(new StoreObject[objects.size()]);
    }

    public void deleteObject(StoreObject object) {
        getObject(object); // check for existence
        objects.remove(object);
    }
}
