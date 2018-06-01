package htw.vs1.filesystem;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by markus on 23.06.15.
 */
public class CommandParserTest {


    private CommandParser test;
    @Before
    public void setUp() throws Exception {
        test = new CommandParser();
    }

    @Test
    public void testEmptyCommand() throws Exception {
        try {
            test.parse("");
            assertEquals("getCommand should return empty String",test.getCommand(), "");
            assertEquals("getArgs should return empty String Array",test.getArgs().length, 0);
        } catch(Exception e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            fail("Empty String should not throw an exception");
        }
    }

    @Test
    public void testGetArgs() throws Exception {
        test.parse("cmd arg1 arg2");

        assertEquals("Command string is not correct.", "cmd", test.getCommand());
        assertEquals("Argument 1 is not correct." , "arg1", test.getArgs()[0]);
        assertEquals("Argument 2 is not correct." , "arg2", test.getArgs()[1]);
    }

    @Test
    public void testGetCommand() throws Exception {

    }
}