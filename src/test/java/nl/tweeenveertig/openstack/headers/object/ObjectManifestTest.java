package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

public class ObjectManifestTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectManifest("images/extremely-big-file.dat"));
    }
}
