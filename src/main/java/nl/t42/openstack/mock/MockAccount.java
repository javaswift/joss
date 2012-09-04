package nl.t42.openstack.mock;

import nl.t42.openstack.model.AccountInformation;
import nl.t42.openstack.model.Container;

import java.util.Map;
import java.util.TreeMap;

public class MockAccount {

    private Map<Container, MockContainer> containers = new TreeMap<Container, MockContainer>();

    private AccountInformation info;

}
