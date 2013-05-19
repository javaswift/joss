package org.javaswift.joss.command.shared.identity;

import org.javaswift.joss.command.shared.core.Command;
import org.javaswift.joss.model.Access;

public interface AuthenticationCommand extends Command<Access> {

    String getUrl();
}
