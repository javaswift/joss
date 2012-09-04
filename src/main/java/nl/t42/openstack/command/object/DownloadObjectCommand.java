package nl.t42.openstack.command.object;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.*;

public class DownloadObjectCommand extends AbstractObjectCommand<HttpGet, byte[]> {

    public static final String ETAG             = "ETag";
    public static final String CONTENT_LENGTH   = "Content-Length";

    public DownloadObjectCommand(HttpClient httpClient, Access access, Container container, StoreObject object) throws IOException {
        super(httpClient, access, container, object);
    }

    @Override
    protected byte[] getReturnObject(HttpResponse response) throws IOException {
        String expectedMd5 = response.getHeaders(ETAG)[0].getValue();
        int contentLength = Integer.parseInt(response.getHeaders(CONTENT_LENGTH)[0].getValue());

        InputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            input = response.getEntity().getContent();
            output = new ByteArrayOutputStream(contentLength);
            byte[] buffer = new byte[1024];
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            byte[] result = output.toByteArray();
            String realMd5 = new String(Hex.encodeHex(DigestUtils.md5(result)));
            if (!expectedMd5.equals(realMd5)) {
                throw new CommandException(HttpStatus.SC_UNPROCESSABLE_ENTITY, CommandExceptionError.MD5_CHECKSUM);
            }
            return result;
        } finally {
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
            if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
        }
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_OK), null),
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND), CommandExceptionError.CONTAINER_DOES_NOT_EXIST)
        };
    }

}
