package nl.tweeenveertig.openstack.mock;

import nl.tweeenveertig.openstack.headers.object.Etag;
import nl.tweeenveertig.openstack.headers.object.ObjectContentLength;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import nl.tweeenveertig.openstack.headers.object.ObjectMetadata;
import nl.tweeenveertig.openstack.model.ObjectInformation;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.util.MimeTypeMap;
import org.apache.commons.codec.digest.DigestUtils;

public class MockObject extends AbstractMock<ObjectInformation, ObjectMetadata> {

    private byte[] object;

    private String md5;

    private String contentType;

    public void saveObject(StoredObject name, byte[] object) {
        this.object = object;
        this.md5 = DigestUtils.md5Hex(object);
        this.contentType = MimeTypeMap.getContentType(name.getName());
    }

    public byte[] getObject() {
        return object;
    }

    @Override
    protected void appendInformation(ObjectInformation info) {
        info.setContentLength(new ObjectContentLength(Integer.toString(this.object.length)));
        info.setEtag(new Etag(this.md5));
        info.setContentType(new ObjectContentType(this.contentType));
        //info.setLastModified();
    }

    @Override
    protected ObjectInformation createInformationContainer() {
        return new ObjectInformation();
    }

    @Override
    protected ObjectMetadata createMetadata(String name, String value) {
        return new ObjectMetadata(name, value);
    }
}
