package htw.vs1.filesystem.FileSystem.virtual;

import htw.vs1.filesystem.FileSystem.exceptions.ObjectNotFoundException;
import htw.vs1.filesystem.FileSystemManger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by markus on 04.06.15.
 */
public class FileSystemInterfaceTest {


    private FileSystemInterface fs;
    private Folder reference;

    @Before
    public void setUp() throws Exception {

        //Hier die eigene Implementierung vermerken!


        Folder root = new LocalFolder("root"); // the name of the root folder will always be skipped -> abs.Path=/
        root.add(new LocalFile("datei1"));
        root.add(new LocalFile("datei2"));

        Folder subFolder = new LocalFolder("sub");
        subFolder.add(new LocalFolder("subsub"));
        subFolder.add(new LocalFile("datei1"));
        subFolder.add(new LocalFile("datei2"));

        root.add(subFolder);
        this.reference = root;

        this.fs = new FileSystem(root, true);
    }

    @After
    public void tearDown() throws Exception {
        this.fs = null;
    }

    @Test
    public void testSetWorkingDirectory() throws Exception {
        Folder testFolder = new LocalFolder("testFolder");
        this.fs.setWorkingDirectory(testFolder);

        assertEquals(testFolder, this.fs.getWorkingDirectory());


    }

    @Test
    public void testGetWorkingDirectory() throws Exception {
        assertEquals(this.reference, this.fs.getWorkingDirectory());
    }

    @Test
    public void testChangeDirectory() throws Exception {

        try {
            //Ordner existiert nicht
            this.fs.changeDirectory("does-not-exist");
            fail("Ordner exisitiert nicht. Erwartete Exception ObjectNotFoundException wurde nicht geworfen.");
        } catch (ObjectNotFoundException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            //Alles in Ordnung
        }


        try {
            //übergebener Name ist eine Datei
            this.fs.changeDirectory("datei1");
            fail("übergebener Name gehört zu einer Datei. Erwartete Exception ObjectNotFoundException wurde nicht geworfen.");
        } catch (ObjectNotFoundException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            //.. Alles in Ordnung
        }

        String subFolder = "sub";
        this.fs.changeDirectory(subFolder);
        //In korrekten Unterordner gewechselt?
        assertEquals(this.fs.getWorkingDirectory().getName(), subFolder);


    }

    @Test
    public void testListDirectoryContent() throws Exception {
        List<FSObject> out = this.fs.listDirectoryContent();
        String allElements = FSObject.printFSObjectList(out, false);
        for(FSObject el: this.reference.getContent()) {
            if(!allElements.contains(el.getName())) fail("List Directory gibt nicht alle Dateinamen aus, die vorhanden sind.");
        }
    }

    @Test
    public void testPrintWorkingDirectory() throws Exception {
        this.fs.changeDirectory("sub");
        this.fs.changeDirectory("subsub");
        String out = this.fs.printWorkingDirectory();

        assertEquals("/sub/subsub", out);

    }
}