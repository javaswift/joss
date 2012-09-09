package nl.t42.openstack.command.object;

import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.commons.codec.digest.DigestUtils;
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
        InputStream input = null;
        OutputStream output = null;
        try {
            input = entity.getContent();
            output = new FileOutputStream(targetFile);
            byte[] buffer = new byte[65536];
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
        } finally {
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
            if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
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
