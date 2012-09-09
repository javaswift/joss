package nl.t42.openstack.command.object;

import nl.t42.openstack.command.identity.access.Access;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.commons.codec.digest.DigestUtils;
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
    protected OutputStream openOutputStream() throws FileNotFoundException {
        return new FileOutputStream(targetFile);
    }

    @Override
    protected String getMd5(OutputStream output) throws IOException {
        FileInputStream input = null;
        try {
            input = new FileInputStream(targetFile);
            return DigestUtils.md5Hex(input);
        } finally {
            if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
        }
    }

    @Override
    protected Object getObjectAsReturnObject(OutputStream output) {
        return null;
    }

}
