package nl.t42.openstack.mock;

import nl.t42.openstack.model.ObjectInformation;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MockObjectTest {

    @Test
    public void saveObject() {
        MockObject object = new MockObject();
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.saveObject(bytes);
        assertEquals(bytes.length, object.getObject().length);
    }

    @Test
    public void getInfo() {
        MockObject object = new MockObject();
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.saveObject(bytes);
        ObjectInformation info = object.getInfo();
        assertEquals(3, info.getContentLength());
        assertEquals("5289df737df57326fcdd22597afb1fac", info.getEtag());
    }
}
