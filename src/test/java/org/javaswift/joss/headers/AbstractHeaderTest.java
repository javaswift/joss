package org.javaswift.joss.headers;

import mockit.Mocked;
import mockit.Verifications;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractHeaderTest {

    @Mocked private HttpRequestBase request;

    protected void testHeader(final Header header) {
        header.setHeader(request);
        new Verifications() {{
            request.setHeader(header.getHeaderName(), header.getHeaderValue());
        }};
    }

}