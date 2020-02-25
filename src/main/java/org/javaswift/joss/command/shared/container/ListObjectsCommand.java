package org.javaswift.joss.command.shared.container;

import java.util.Collection;

import org.javaswift.joss.command.shared.core.Command;
import org.javaswift.joss.model.StoredObject;

public interface ListObjectsCommand extends Command<Collection<StoredObject>> {
}
