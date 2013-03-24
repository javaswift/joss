package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.impl.core.httpstatus.*;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.headers.object.*;
import org.javaswift.joss.information.ObjectInformation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class ObjectInformationCommand extends AbstractObjectCommand<HttpHead, ObjectInformation> {

    public ObjectInformationCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject object) {
        super(account, httpClient, access, object);
    }

    @Override
    protected ObjectInformation getReturnObject(HttpResponse response) throws IOException {
        ObjectInformation info = new ObjectInformation();
        info.setMetadata(ObjectMetadata.fromResponse(response));
        info.setLastModified(ObjectLastModified.fromResponse(response));
        info.setEtag(Etag.fromResponse(response));
        info.setContentLength(ObjectContentLength.fromResponse(response));
        info.setContentType(ObjectContentType.fromResponse(response));
        info.setDeleteAt(DeleteAt.fromResponse(response));
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusRange(200, 299)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }
}
