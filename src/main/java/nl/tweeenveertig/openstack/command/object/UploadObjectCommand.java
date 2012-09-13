package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.impl.AccountImpl;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UploadObjectCommand extends AbstractObjectCommand<HttpPut, Object> {

    public static final String ETAG = "ETag";

    public UploadObjectCommand(AccountImpl account, HttpClient httpClient, Access access, Container container, StoredObject target, InputStream inputStream) {
        super(account, httpClient, access, container, target);
        try {
            prepareUpload(new InputStreamEntity(inputStream, -1));
        } catch (IOException err) {
            throw new CommandException("Unable to open input stream for uploading", err);
        }
    }

    public UploadObjectCommand(AccountImpl account, HttpClient httpClient, Access access, Container container, StoredObject target, File fileToUpload) {
        super(account, httpClient, access, container, target);
        try {
            prepareUpload(new FileEntity(fileToUpload));
        } catch (IOException err) {
            throw new CommandException("Unable to open the file for uploading: "+fileToUpload.getAbsolutePath(), err);
        }
    }

    public UploadObjectCommand(AccountImpl account, HttpClient httpClient, Access access, Container container, StoredObject target, byte[] fileToUpload) {
        super(account, httpClient, access, container, target);
        try {
            prepareUpload(new ByteArrayEntity(fileToUpload));
        } catch (IOException err) {
            throw new CommandException("Unable to open the byte[] for uploading", err);
        }
    }

    protected void prepareUpload(HttpEntity entity) throws IOException {
        if (!(entity instanceof InputStreamEntity)) { // reading an InputStream is not a smart idea
            request.addHeader(ETAG, DigestUtils.md5Hex(entity.getContent()));
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
