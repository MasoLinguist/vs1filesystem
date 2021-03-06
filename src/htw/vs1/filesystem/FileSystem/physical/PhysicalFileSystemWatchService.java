package htw.vs1.filesystem.FileSystem.physical;

import htw.vs1.filesystem.FileSystem.exceptions.*;
import htw.vs1.filesystem.FileSystem.virtual.*;
import htw.vs1.filesystem.FileSystemManger;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Service to watch a directory on the physical file system
 * and apply all the changes in our virtual file system.
 *
 * Created by felix on 17.06.15.
 */
public class PhysicalFileSystemWatchService extends AbstractWatchService {

    private boolean DEBUG_MODE = false;

    /**
     * instance of our virtual file system.
     */
    private FileSystemInterface fileSystem;

    /**
     * Creates a WatchService and registers the given directory
     */
    PhysicalFileSystemWatchService() throws IOException {
        fileSystem = FileSystemManger.getInstance().getFileSystem(false);
        resetWorkingDirectory();
    }

    /**
     * Resets the working directory to the root folder.
     */
    private void resetWorkingDirectory() {
        try {
            fileSystem.setWorkingDirectory(FileSystemManger.getInstance().getRootFolder());
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the working directory from the root folder
     * to the folder represented by the given {@link Path}.
     *
     * @param pathToWorkingDir Path linked to the desired folder.
     * @throws ObjectNotFoundException iff the path could not be resolved.
     */
    private void changeWorkingDirectory(Path pathToWorkingDir) throws FileSystemException {
        String relativePathString = FileSystemManger.getInstance().getRootFolder().getPath().relativize(pathToWorkingDir).toString();
        if (!relativePathString.isEmpty()){
            fileSystem.changeDirectory(relativePathString);
        }
    }

    /**
     * Gets the virtual representation of the affected
     * object, iff it was found.
     *
     * @param pathToObject path to the affected object.
     * @return the virtual representation of the affected
     *          object, {@code null} iff it was not found.
     * @throws ObjectNotFoundException
     */
    private LocalFSObject getAffectedObject(Path pathToObject, Path pathToParent) throws FileSystemException {
        // Change the working directory to the affected directory
        changeWorkingDirectory(pathToParent);
        FSObject affected;

        try {
            // get the object represented by the name
            affected = fileSystem.getWorkingDirectory().getObject(pathToObject.toFile().getName());
        } catch (ObjectNotFoundException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            return null;
        }

        if (affected instanceof LocalFSObject) {
            return (LocalFSObject) affected;
        }

        return null;
    }

    @Override
    protected void onEntryDelete(Path child, Path dir) {
        if (DEBUG_MODE) {
            System.out.format("Deleted file/folder %s in directory %s\n", child.toFile().getName(), dir);
            System.out.format("Relative path from local-root: %s\n",
                    FileSystemManger.getInstance().getRootFolder().getPath().relativize(dir));
        }

        try {
            // get the FSObject which should be deleted
            LocalFSObject toDelete = getAffectedObject(child, dir);
            if (null != toDelete) {
                toDelete.setPath(null);
                fileSystem.getWorkingDirectory().delete(toDelete);
            }

        } catch (Exception e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
        }

        // reset the working directory to the root folder.
        resetWorkingDirectory();
    }

    @Override
    protected void onEntryCreate(Path child, Path dir) {
        if (DEBUG_MODE) {
            System.out.format(
                    "Created %s %s in directory %s\n",
                    (child.toFile().isDirectory()) ? "folder" : "file",
                    child.toFile().getName(),
                    dir);
        }


        try {
            if (fileSystem.getWorkingDirectory().exists(child.toFile().getName())){
                // the file was created by our program. Although we have to add the new object
                // to the watch list iff it is a folder.
                    FSObject object = fileSystem.getWorkingDirectory().getObject(child.toFile().getName());
                    if (object instanceof  LocalFolder) {
                        PhysicalFileSystemAdapter.getInstance().registerDirectoryToWatchService(child);
                    }
                return;
            }
        } catch (FileSystemException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            // this should never happen because we already checked if the object exists...
            throw new IllegalStateException("Object doesn't exists even though #exists evaluates to true.", e);
        } catch (IOException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
        }

        try {
            changeWorkingDirectory(dir);

            LocalFSObject toCreate;
            if (child.toFile().isFile()) {
                toCreate = new LocalFile(child.toFile().getName(),child);
            } else {
                toCreate = new LocalFolder(child.toFile().getName(),child);
            }

            fileSystem.getWorkingDirectory().add(toCreate);

            if (toCreate instanceof  LocalFolder) {
                PhysicalFileSystemAdapter.getInstance().registerDirectoryToWatchService(child);
                PhysicalFileSystemAdapter.getInstance().loadFileSystemDirectories(child, (LocalFolder) toCreate);
            }
        } catch (FSObjectAlreadyExistsException e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
            // no problem, it look like the object was created by our file system ;-)
        }
        catch (Exception e) {
            if (FileSystemManger.DEBUG) {
                e.printStackTrace();
            }
        }

        resetWorkingDirectory();
    }

}
