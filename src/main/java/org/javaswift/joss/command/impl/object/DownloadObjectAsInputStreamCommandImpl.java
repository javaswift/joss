package org.javaswift.joss.command.impl.object;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.javaswift.joss.command.shared.object.DownloadObjectAsInputStreamCommand;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

import java.io.IOException;
import java.io.InputStream;

public class DownloadObjectAsInputStreamCommandImpl extends AbstractDownloadObjectCommand<HttpGet, InputStream> implements DownloadObjectAsInputStreamCommand {

    private InputStream inputStream;

    public DownloadObjectAsInputStreamCommandImpl(Account account, HttpClient httpClient, Access access,
                                                  StoredObject object, DownloadInstructions downloadInstructions) {
        super(account, httpClient, access, object, downloadInstructions);
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
    protected InputStream getObjectAsReturnObject() {
        return inputStream;
    }

    @Override
    protected boolean closeStreamAutomatically() {
        return false;
    }

}
