package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoreObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public class DownloadObjectAsInputStreamCommand extends AbstractDownloadObjectCommand<HttpGet, InputStreamWrapper> {

    private InputStreamWrapper inputStream;

    public DownloadObjectAsInputStreamCommand(HttpClient httpClient, Access access, Container container, StoreObject object) {
        super(httpClient, access, container, object);
    }

    @Override
    protected void handleEntity(HttpEntity entity) throws IOException {
        inputStream = new InputStreamWrapper(this, entity.getContent());
    }

    @Override
    protected String getMd5() throws IOException {
        return null;
    }

    @Override
    protected InputStreamWrapper getObjectAsReturnObject() {
        return inputStream;
    }

    @Override
    protected boolean closeStreamAutomatically() {
        return false;
    }

}
