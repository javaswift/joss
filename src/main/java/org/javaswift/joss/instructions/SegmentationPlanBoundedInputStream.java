package org.javaswift.joss.instructions;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.BoundedInputStream;

public class SegmentationPlanBoundedInputStream extends SegmentationPlan {

	private long length;
	private InputStream inputStream;

	public SegmentationPlanBoundedInputStream(InputStream inputStream, long length, Long segmentationSize) {
		super(segmentationSize);
		this.length = length;
		this.inputStream = inputStream;
	}

	@Override
	protected Long getFileLength() {
		return length;
	}

	@Override
	protected InputStream createSegment() throws IOException {
		BoundedInputStream stream = new BoundedInputStream(inputStream, segmentationSize);
		stream.setPropagateClose(false);
		return stream;
	}

	@Override
	public void close() throws IOException {
		inputStream.close();
	}

}
