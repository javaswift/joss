package nl.t42.openstack.command.object;

import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class DownloadObjectAsByteArrayCommand extends AbstractDownloadObjectCommand<HttpGet, byte[]> {

    private byte[] result;

    public DownloadObjectAsByteArrayCommand(HttpClient httpClient, Access access, Container container, StoreObject object) {
        super(httpClient, access, container, object);
    }

    @Override
    protected void handleEntity(HttpEntity entity) throws IOException {
        result = EntityUtils.toByteArray(entity);
    }

    @Override
    protected String getMd5() throws IOException {
        return DigestUtils.md5Hex(result);
    }

    @Override
    protected byte[] getObjectAsReturnObject() {
        return result;
    }
}
