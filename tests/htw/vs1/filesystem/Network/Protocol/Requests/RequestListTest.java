package htw.vs1.filesystem.Network.Protocol.Requests;

import htw.vs1.filesystem.FileSystemManger;
import htw.vs1.filesystem.Network.Protocol.Requests.RequestLinkedList;
import htw.vs1.filesystem.Network.Protocol.Requests.RequestList;
import htw.vs1.filesystem.Network.Protocol.Requests.SimpleProtocolRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

/**
 * Created by markus on 14.06.15.
 */
public class RequestListTest {


    private RequestLinkedList list = new RequestLinkedList();


    @Before
    public void setUp() throws Exception {
        list.add(new SimpleProtocolRequest("A"));
        list.add(new SimpleProtocolRequest("B 123 5566"));
        list.add(new SimpleProtocolRequest("C 112 32 4222"));

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetPreviousElement() throws Exception {
        assertEquals("Does not return previous element",list.getPreviousElement().getCommandString(), "B");

    }

    @Test
    public void testGetCurrentElement() throws Exception {

        assertEquals("Does not return current element",list.getCurrentElement().getCommandString(), "C");



    }

    @Test
    public void testGetNthElementFromEnd() throws Exception {
        try {
            list.getNthElementFromEnd(-1);
            fail("Expected IndexOutOfBoundsException for illegal n");
        } catch (IndexOutOfBoundsException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            //Alles okay
        }

        try {
            list.getNthElementFromEnd(42);
            fail("Expected IndexOutOfBoundsException for invalid n");
        } catch (IndexOutOfBoundsException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            //Alles okay
        }

        assertTrue(list.getNthElementFromEnd(1).getCommandString().equals("B"));


    }

    @Test
    public void testUnmodifiableRequestList() throws Exception {
        for(Method meth: RequestList.class.getDeclaredMethods()) {
            for(Type type: meth.getParameterTypes()) {
                if (type.getTypeName().equals("htw.vs1.filesystem.Network.Protocol.Requests.Request")) {
                    fail("Interface allows to add content.");
                }
            }
            if (meth.getName().contains("del") || meth.getName().contains("loeschen")) {
                fail("Interface seems to allow deletion of content");
            }
        }


    }
}