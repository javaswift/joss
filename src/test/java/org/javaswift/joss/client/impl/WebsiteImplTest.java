package org.javaswift.joss.client.impl;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.model.Website;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;

public class WebsiteImplTest extends BaseCommandTest {

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getObject() {
        Website website = account.getWebsite("website");
        StoredObject object = website.getObject("index.html");
        assertTrue(object instanceof StoredObjectImpl);
    }
}
