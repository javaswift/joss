package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoreObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.*;

public class DownloadObjectToFileCommand extends AbstractDownloadObjectCommand<HttpGet, Object> {

    private File targetFile;

    public DownloadObjectToFileCommand(HttpClient httpClient, Access access, Container container, StoreObject object, File targetFile) {
        super(httpClient, access, container, object);
        this.targetFile = targetFile;
    }

    @Override
    protected void handleEntity(HttpEntity entity) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(targetFile);
            IOUtils.copy(entity.getContent(), new FileOutputStream(targetFile));
        } finally {
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
        }
    }

    @Override
    protected String getMd5() throws IOException {
        FileInputStream input = null;
        try {
            input = new FileInputStream(targetFile);
            return DigestUtils.md5Hex(input);
        } finally {
            if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
        }
    }

    @Override
    protected Object getObjectAsReturnObject() {
        return null;
    }

}
