package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.httpstatus.*;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.headers.object.*;
import nl.tweeenveertig.openstack.information.ObjectInformation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class ObjectInformationCommand extends AbstractObjectCommand<HttpHead, ObjectInformation> {

    public ObjectInformationCommand(Account account, HttpClient httpClient, Access access, Container container, StoredObject object) {
        super(account, httpClient, access, container, object);
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
