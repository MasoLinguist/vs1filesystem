package htw.vs1.filesystem.FileSystem.virtual;

import htw.vs1.filesystem.FileSystem.exceptions.InvalidFilenameException;
import htw.vs1.filesystem.FileSystemManger;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Myx1n on 19.06.2015.
 */
public class LocalFileTest {


    /**
     * Tests to rename a file with an invalid filename.
     */
    @Test
    public void testSetName() throws Exception {
        LocalFile file = new LocalFile("Test.txt");

        try {
            file.setName(".-/?");
            fail("delete(File) should throw InvalidFilenameException, when trying to setname of a " +
                    "File that already exists.");
        } catch (InvalidFilenameException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            // Alles OK
        }
    }

    @Test
    public void testGetParentFolder() throws Exception {

    }

    @Test
    public void testSetParentFolder() throws Exception {

       // root.setParentFolder();
    }

    @Test
    public void testGetPath() throws Exception {

    }

    @Test
    public void testSetPath() throws Exception {

    }

    @Test
    public void testDelete1() throws Exception {

    }

    @Test
    public void testCheckPrecondition() throws Exception {

    }

    @Test
    public void testSetName1() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {

    }

    @Test
    public void testGetParentFolder1() throws Exception {

    }

    @Test
    public void testSetParentFolder1() throws Exception {

    }

    @Test
    public void testGetAbsolutePath() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }
}