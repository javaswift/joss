package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.UploadInstructions;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.headers.Etag;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.params.CoreProtocolPNames;

import java.io.IOException;

public class UploadObjectCommand extends AbstractObjectCommand<HttpPut, Object> {

    public UploadObjectCommand(Account account, HttpClient httpClient, Access access, Container container,
                               StoredObject target, UploadInstructions uploadInstructions) {
        super(account, httpClient, access, container, target);
        try {
            prepareUpload(uploadInstructions.getEntity());
        } catch (IOException err) {
            throw new CommandException("Unable to open input stream for uploading", err);
        }
    }

    protected void prepareUpload(HttpEntity entity) throws IOException {
        if (!(entity instanceof InputStreamEntity)) { // reading an InputStream is not a smart idea
            addHeader(new Etag(entity.getContent()));
        }
        request.setEntity(entity);
    }

    @Override
    protected HttpPut createRequest(String url) {
        HttpPut putMethod = new HttpPut(url);
        putMethod.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
        return putMethod;
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_CREATED), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_LENGTH_REQUIRED), CommandExceptionError.MISSING_CONTENT_LENGTH_OR_TYPE),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_UNPROCESSABLE_ENTITY), CommandExceptionError.MD5_CHECKSUM)
        };
    }

}
