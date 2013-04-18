package org.javaswift.joss.command.impl.object;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.object.DownloadObjectToFileCommand;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

import java.io.*;

public class DownloadObjectToFileCommandImpl extends AbstractDownloadObjectCommand<HttpGet, Object> implements DownloadObjectToFileCommand {

    private File targetFile;

    public DownloadObjectToFileCommandImpl(Account account, HttpClient httpClient, AccessImpl access,
                                           StoredObject object, DownloadInstructions downloadInstructions, File targetFile) {
        super(account, httpClient, access, object, downloadInstructions);
        this.targetFile = targetFile;
    }

    @Override
    protected void handleEntity(HttpEntity entity) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(targetFile);
            IOUtils.copy(entity.getContent(), output);
        } finally {
            close(output);
        }
    }

    @Override
    protected String getMd5() throws IOException {
        InputStream input = null;
        try {
            input = new FileInputStream(targetFile);
            return DigestUtils.md5Hex(input);
        } finally {
            close(input);
        }
    }

    protected void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

    @Override
    protected Object getObjectAsReturnObject() {
        return null;
    }

}
