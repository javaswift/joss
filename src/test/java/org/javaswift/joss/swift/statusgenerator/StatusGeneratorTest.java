package org.javaswift.joss.swift.statusgenerator;

import org.javaswift.joss.command.mock.account.AccountInformationCommandMock;
import org.javaswift.joss.command.mock.account.AccountMetadataCommandMock;
import org.javaswift.joss.command.mock.account.ListContainersCommandMock;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.account.AccountInformationCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StatusGeneratorTest {

    @Test
    public void successThenFailure() {
        StatusGenerator genner = new StatusGenerator();
        genner.addIgnore();
        genner.addIgnore();
        genner.addStatus(404);
        assertEquals(-1, genner.getStatus(CommandMock.class));
        assertEquals(-1, genner.getStatus(CommandMock.class));
        assertEquals(404, genner.getStatus(CommandMock.class));
        assertEquals(-1, genner.getStatus(CommandMock.class));
    }

    @Test
    public void mixAndMash() {
        StatusGenerator genner = new StatusGenerator();
        genner.addStatus(404);
        genner.addIgnore(AccountInformationCommandMock.class);
        genner.addIgnore(ListContainersCommandMock.class);
        genner.addIgnore(ListContainersCommandMock.class);
        genner.addStatus(ListContainersCommandMock.class, 500);

        assertEquals(-1, genner.getStatus(AccountInformationCommandMock.class));

        assertEquals(-1, genner.getStatus(ListContainersCommandMock.class));
        assertEquals(-1, genner.getStatus(ListContainersCommandMock.class));
        assertEquals(500, genner.getStatus(ListContainersCommandMock.class));
        assertEquals(-1, genner.getStatus(ListContainersCommandMock.class));

        assertEquals(404, genner.getStatus(AccountMetadataCommandMock.class));
    }

}
