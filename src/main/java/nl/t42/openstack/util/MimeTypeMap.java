package nl.t42.openstack.util;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

public class MimeTypeMap {

    private static MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();

    public static String getContentType(String fileName) {
        return mimeTypesMap.getContentType(fileName);
    }

    public static String getContentType(File file) {
        return mimeTypesMap.getContentType(file);
    }

}
