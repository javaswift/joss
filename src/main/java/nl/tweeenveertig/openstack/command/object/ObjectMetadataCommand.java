package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.headers.metadata.Metadata;
import nl.tweeenveertig.openstack.headers.metadata.ObjectMetadata;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.Map;

public class ObjectMetadataCommand extends AbstractObjectCommand<HttpPost, Object> {

    public ObjectMetadataCommand(Account account, HttpClient httpClient, Access access, Container container, StoredObject object, Map<String, Object> metadata) {
        super(account, httpClient, access, container, object);
        addMetadata(metadata);
    }

    protected void addMetadata(Map<String, Object> metadata) {
        for (String name : metadata.keySet()) {
            addHeader(new ObjectMetadata(name, metadata.get(name).toString()));
        }
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_ACCEPTED), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST)
        };
    }

}
