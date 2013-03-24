package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.model.StoredObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.*;

public class DownloadObjectToFileCommand extends AbstractDownloadObjectCommand<HttpGet, Object> {

    private File targetFile;

    public DownloadObjectToFileCommand(Account account, HttpClient httpClient, AccessImpl access,
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
            if (output != null) {
                try {
                    output.close();
                } catch (IOException logOrIgnore) { }
            }
        }
    }

    @Override
    protected String getMd5() throws IOException {
        InputStream input = null;
        try {
            input = new FileInputStream(targetFile);
            return DigestUtils.md5Hex(input);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException logOrIgnore) {}
            }
        }
    }

    @Override
    protected Object getObjectAsReturnObject() {
        return null;
    }

}
