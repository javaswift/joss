package nl.t42.openstack.command.object;

import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.*;

public class DownloadObjectAsByteArrayCommand extends AbstractDownloadObjectCommand<HttpGet, byte[]> {

    private int contentLength;

    private byte[] result;

    public DownloadObjectAsByteArrayCommand(HttpClient httpClient, Access access, Container container, StoreObject object) {
        super(httpClient, access, container, object);
    }

    @Override
    protected byte[] getReturnObject(HttpResponse response) throws IOException {
        contentLength = Integer.parseInt(response.getHeaders(CONTENT_LENGTH)[0].getValue());
        return super.getReturnObject(response);
    }

    @Override
    protected OutputStream openOutputStream() throws FileNotFoundException {
        return new ByteArrayOutputStream(contentLength);
    }

    @Override
    protected String getMd5(OutputStream output) throws IOException {
        result = ((ByteArrayOutputStream)output).toByteArray();
        return DigestUtils.md5Hex(result);
    }

    @Override
    protected byte[] getObjectAsReturnObject(OutputStream output) {
        return result;
    }
}
