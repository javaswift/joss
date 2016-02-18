package org.javaswift.joss.headers.object;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.SimpleHeader;

/**
 * Created by yuyg on 16/2/18.
 */
public class ObjectSLO extends SimpleHeader {
    public static String X_OBJECT_SLO = "X-Static-Large-Object";

    public ObjectSLO(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_OBJECT_SLO;
    }

    public static ObjectSLO fromResponse(HttpResponse response) {
        return new ObjectSLO(convertResponseHeader(response, X_OBJECT_SLO));
    }
}
