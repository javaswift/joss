package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class DownloadObjectAsByteArrayCommand extends AbstractDownloadObjectCommand<HttpGet, byte[]> {

    private byte[] result;

    public DownloadObjectAsByteArrayCommand(Account account, HttpClient httpClient, AccessImpl access,
                                            StoredObject object, DownloadInstructions downloadInstructions) {
        super(account, httpClient, access, object, downloadInstructions);
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
