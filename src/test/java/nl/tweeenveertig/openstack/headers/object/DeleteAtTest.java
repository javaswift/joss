package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static nl.tweeenveertig.openstack.command.core.BaseCommandTest.prepareHeader;
import static nl.tweeenveertig.openstack.headers.object.DeleteAt.X_DELETE_AT;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteAtTest extends HeaderTest {

    @Mock
    private HttpResponse response;

    @Test
    public void testAddHeaderByString() throws DateParseException {
        testHeader(new DeleteAt("Sat, 22 Sep 2012 07:24:21 GMT"));
    }

    @Test
    public void testAddHeaderByMilliseconds() throws DateParseException {
        testHeader(new DeleteAt(DateUtils.parseDate("Sat, 22 Sep 2012 07:24:21 GMT").getTime()));
    }

    @Test
    public void testAddHeaderByDate() throws DateParseException {
        testHeader(new DeleteAt(DateUtils.parseDate("Sat, 22 Sep 2012 07:24:21 GMT")));
    }

    @Test
    public void noLegalDeleteAtDate() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_DELETE_AT, "I'm definitely not a date!", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
        assertNull(DeleteAt.fromResponse(response));
    }
}
