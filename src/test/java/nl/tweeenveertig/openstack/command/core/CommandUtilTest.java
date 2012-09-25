package nl.tweeenveertig.openstack.command.core;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CommandUtilTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void emptyResponse() throws IOException {
        new CommandUtil(); // Fool Cobertura
        when(response.getEntity()).thenReturn(null);
        assertEquals(0, CommandUtil.convertResponseToString(response).size());
    }

    @Test
    public void convertResponseBodyToLines() throws IOException {
        InputStream inputStream = IOUtils.toInputStream(
                "Amsterdam\n"+
                "Den Haag\n"+
                "Zoetermeer\n"+
                "Leiden");
        when(httpEntity.getContent()).thenReturn(inputStream);
        when(response.getEntity()).thenReturn(httpEntity);
        List<String> lines = CommandUtil.convertResponseToString(response);
        assertEquals(4, lines.size());
    }
}
