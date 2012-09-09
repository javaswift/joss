package nl.t42.openstack.command.object;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.core.HttpStatusChecker;
import nl.t42.openstack.command.core.HttpStatusMatch;
import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.*;

public abstract class AbstractDownloadObjectCommand<M extends HttpGet, N extends Object> extends AbstractObjectCommand<HttpGet, N> {

    public static final String ETAG             = "ETag";
    public static final String CONTENT_LENGTH   = "Content-Length";

    public AbstractDownloadObjectCommand(HttpClient httpClient, Access access, Container container, StoreObject object) {
        super(httpClient, access, container, object);
    }

    @Override
    protected N getReturnObject(HttpResponse response) throws IOException {
        String expectedMd5 = response.getHeaders(ETAG)[0].getValue();

        InputStream input = null;
        OutputStream output = null;
        try {
            input = response.getEntity().getContent();
            output = openOutputStream();
            byte[] buffer = new byte[65536];
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            if (!expectedMd5.equals(getMd5(output))) {
                throw new CommandException(HttpStatus.SC_UNPROCESSABLE_ENTITY, CommandExceptionError.MD5_CHECKSUM);
            }
            return getObjectAsReturnObject(output);
        } finally {
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
            if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
        }
    }

    protected abstract OutputStream openOutputStream() throws FileNotFoundException;

    protected abstract String getMd5(OutputStream outputStream) throws IOException;

    protected abstract N getObjectAsReturnObject(OutputStream output);

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
