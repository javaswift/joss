package org.javaswift.joss.instructions;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

public class SegmentationPlanTestBase {

    protected void assertTextEquals(SegmentationPlan plan, String assertText) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        InputStream inputStream = plan.getNextSegment();
        while (inputStream != null) {
            int readChar = inputStream.read();
            while (readChar != -1) {
                stringBuffer.append((char) readChar);
                readChar = inputStream.read();
            }
            inputStream.close();
            inputStream = plan.getNextSegment();
        }
        plan.close();
        assertEquals(assertText, stringBuffer.toString());
    }
}
