package nl.t42.openstack.mock;

import nl.t42.openstack.model.ObjectInformation;
import nl.t42.openstack.model.StoreObject;
import nl.t42.openstack.util.MimeTypeMap;
import org.apache.commons.codec.digest.DigestUtils;

public class MockObject extends AbstractMock<ObjectInformation> {

    private byte[] object;

    private String md5;

    private String contentType;

    public void saveObject(StoreObject name, byte[] object) {
        this.object = object;
        this.md5 = DigestUtils.md5Hex(object);
        this.contentType = MimeTypeMap.getContentType(name.getName());
    }

    public byte[] getObject() {
        return object;
    }

    @Override
    protected void appendInformation(ObjectInformation info) {
        info.setContentLength(this.object.length);
        info.setEtag(this.md5);
        info.setContentType(this.contentType);
        //info.setLastModified();
    }

    @Override
    protected ObjectInformation createInformationContainer() {
        return new ObjectInformation();
    }
}
