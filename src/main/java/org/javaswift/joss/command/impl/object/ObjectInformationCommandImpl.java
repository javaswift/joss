package org.javaswift.joss.command.impl.object;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.object.ObjectInformationCommand;
import org.javaswift.joss.headers.object.DeleteAt;
import org.javaswift.joss.headers.object.Etag;
import org.javaswift.joss.headers.object.ObjectContentLength;
import org.javaswift.joss.headers.object.ObjectContentType;
import org.javaswift.joss.headers.object.ObjectLastModified;
import org.javaswift.joss.headers.object.ObjectManifest;
import org.javaswift.joss.headers.object.ObjectMetadata;
import org.javaswift.joss.information.ObjectInformation;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

public class ObjectInformationCommandImpl extends AbstractObjectCommand<HttpHead, ObjectInformation> implements ObjectInformationCommand {

    public ObjectInformationCommandImpl(Account account, HttpClient httpClient, Access access, StoredObject object, boolean allowErrorLog) {
        super(account, httpClient, access, object);
        setAllowErrorLog(allowErrorLog);
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
        info.setManifest(ObjectManifest.fromResponse(response));
        return info;
    }

    @Override
    protected HttpHead createRequest(String url) {
        return new HttpHead(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusRange(200, 299)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }
}
