package nl.t42.openstack.command.object;

import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;

import java.io.File;
import java.io.IOException;

public class UploadObjectCommand extends AbstractObjectCommand<HttpPut, Object> {

    public static final String ETAG = "ETag";

    public UploadObjectCommand(HttpClient httpClient, Access access, Container container, StoreObject target, File fileToUpload) throws IOException {
        super(httpClient, access, container, target);
        prepareUpload(new FileEntity(fileToUpload));
    }

    public UploadObjectCommand(HttpClient httpClient, Access access, Container container, StoreObject target, byte[] fileToUpload) throws IOException {
        super(httpClient, access, container, target);
        prepareUpload(new ByteArrayEntity(fileToUpload));
    }

    protected void prepareUpload(HttpEntity entity) throws IOException {
        byte[] md5 = DigestUtils.md5(entity.getContent());
        String hexString = new String(Hex.encodeHex(md5));
        request.addHeader(ETAG, hexString);
        request.setEntity(entity);
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
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
