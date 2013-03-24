package org.javaswift.joss.command.shared.container;

import org.javaswift.joss.command.shared.core.Command;
import org.javaswift.joss.model.StoredObject;

import java.util.Collection;

public interface ListObjectsCommand extends Command<Collection<StoredObject>> {
}
