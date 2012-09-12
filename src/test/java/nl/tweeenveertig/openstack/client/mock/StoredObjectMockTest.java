package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.StoredObject;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StoredObjectMockTest {

    private StoredObject object;

    @Before
    public void setup() {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "someObject");
    }

    @Test
    public void saveObject() {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.uploadObject(bytes);
        assertEquals(bytes.length, object.downloadObject().length);
    }

    @Test
    public void getInfo() {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.uploadObject(bytes);
        assertEquals(3, object.getContentLength());
        assertEquals("5289df737df57326fcdd22597afb1fac", object.getEtag());
        assertEquals("text/plain", object.getContentType());
    }
}
