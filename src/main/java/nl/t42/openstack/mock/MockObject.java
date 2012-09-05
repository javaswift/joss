package nl.t42.openstack.mock;

import nl.t42.openstack.model.ObjectInformation;
import org.apache.commons.codec.digest.DigestUtils;

public class MockObject extends AbstractMock<ObjectInformation> {

    private byte[] object;

    private String md5;

    public void saveObject(byte[] object) {
        this.object = object;
        this.md5 = DigestUtils.md5Hex(object);
    }

    public byte[] getObject() {
        return object;
    }

    @Override
    protected void appendInformation(ObjectInformation info) {
        info.setContentLength(this.object.length);
        info.setEtag(this.md5);
        //info.setLastModified();
        //info.setContentType();
    }

    @Override
    protected ObjectInformation createInformationContainer() {
        return new ObjectInformation();
    }
}
