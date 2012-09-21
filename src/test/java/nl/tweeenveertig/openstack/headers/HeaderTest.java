package nl.tweeenveertig.openstack.headers;

import nl.tweeenveertig.openstack.headers.range.AbstractRange;
import nl.tweeenveertig.openstack.headers.range.ExcludeStartRange;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HeaderTest {

    @Test
    public void removeFromHeaderList() {
        List<Header> headers = new ArrayList<Header>();
//        headers.add(new ExcludeStartRange(8));
//        headers.remove(AbstractRange.RAN);
    }
}
