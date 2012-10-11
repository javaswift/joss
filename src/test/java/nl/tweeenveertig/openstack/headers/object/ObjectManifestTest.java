package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class ObjectManifestTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectManifest("images/extremely-big-file.dat"));
    }
}
