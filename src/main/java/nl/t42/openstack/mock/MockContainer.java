package nl.t42.openstack.mock;

import nl.t42.openstack.model.ContainerInformation;
import nl.t42.openstack.model.StoreObject;

import java.util.Map;
import java.util.TreeMap;

public class MockContainer {

    private Map<StoreObject, MockObject> objects = new TreeMap<StoreObject, MockObject>();

    private ContainerInformation info;

}
