package org.javaswift.joss.swift;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest( Swift.class )
public class SwiftTest {

    @Test(expected = CommandException.class)
    public void setOnFileObjectStore() throws Exception {
        OnFileObjectStoreLoader loader = mock(OnFileObjectStoreLoader.class);
        whenNew(OnFileObjectStoreLoader.class).withNoArguments().thenReturn(loader);
        doThrow(new IOException()).when(loader).createContainers(anyString());
        new Swift().setOnFileObjectStore("test");
    }

    @Test
    public void getPublicUrl() {
        assertEquals("", new Swift().getPublicURL());
        Swift swift = new Swift().setPublicUrl("http://localhost:8080/mock");
        assertEquals("http://localhost:8080/mock", swift.getPublicURL());
    }

//    @Test
//    public void setDeleteAfter() {
//        Account account = new ClientMock().setAllowObjectDeleter(false).setAllowEveryone(true).authenticate(null, null, null, null);
//        ObjectDeleter objectDeleter = Mockito.mock(ObjectDeleter.class);
//        ((AccountMock) account).setSwift(new Swift().setObjectDeleter(objectDeleter));
//        StoredObject object = account.getContainer("alpha").getObject("somefile.png");
//        object.setDeleteAfter(10);
//        verify(objectDeleter).scheduleForDeletion(same(object), isA(Date.class));
//        assertNotNull(object.getDeleteAt());
//    }

}
