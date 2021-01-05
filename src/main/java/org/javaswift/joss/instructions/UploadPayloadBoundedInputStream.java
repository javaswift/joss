package org.javaswift.joss.instructions;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.javaswift.joss.headers.object.Etag;

public class UploadPayloadBoundedInputStream extends UploadPayload {

	private InputStream inputStream;
	private long length;

	public UploadPayloadBoundedInputStream(final InputStream inputStream, long length) {
		this.inputStream = inputStream;
		this.length = length;
	}

	@Override
	public HttpEntity getEntity() {
		return new InputStreamEntity(inputStream, -1);
	}

	@Override
	public boolean mustBeSegmented(Long segmentationSize) {
		return length > segmentationSize;
	}

	@Override
	public Etag getEtag() throws IOException {
		return null;
	}

	@Override
	public SegmentationPlan getSegmentationPlan(Long segmentationSize) throws IOException {
		return new SegmentationPlanBoundedInputStream(inputStream, length, segmentationSize);
	}

}
