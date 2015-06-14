package htw.vs1.filesystem.FileSystem;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A LocalFile represents a File in the local filesystem, which
 * is a leaf in a {@link LocalFolder}.
 *
 * Created by felix on 03.06.15.
 */
public class LocalFile extends LocalFSObject implements File{

    /**
     * Reference to the {@link Folder} containing this one.
     */
    private Folder parent;

    /**
     * Path of the Local File in the real Filesystem
     */
    private Path path;

    /**
     * Creates a new LocalFile.
     *
     * @param name name of the new {@link File}.
     */
    public LocalFile(String name) {
        super(name);
    }

    /**
     * Set the new name of a FSObject and modidies its path
     * @param name new name of this object.
     * @throws FileAlreadyExistsException
     */
    @Override
    public void setName(String name) throws FileAlreadyExistsException {
        if (getParentFolder() != null && getParentFolder().exists(name)) {
            throw new FileAlreadyExistsException(name, null, "in Folder: " + getParentFolder().getAbsolutePath());
        }

        if (null != getPath()) {
            try {
                Path newPath = path.resolveSibling(name);
                Files.move(path, newPath);
                setPath(newPath);
            } catch (IOException e) {
                // TODO: What shall I do with this f*cking exception??
                e.printStackTrace();
            }
        }


        super.setName(name); // It is important to set the name after checking if it exists!
    }

    /**
     * Gets the parent {@link Folder} containing this FSObject. Can be
     * {@link null}, iff this is the root-Folder.
     *
     * @return the parent {@link Folder} or {@code null} iff this is the
     * root-Folder.
     */
    @Override
    public Folder getParentFolder() {
        return parent;
    }

    /**
     * Sets the parent {@link Folder} containing this FSObject. Can be
     * {@link null}, iff this is the root-Folder.
     *
     * @param parentFolder the parent {@link Folder} or {@code null} iff this is the
     *                     root-Folder.
     */
    @Override
    protected void setParentFolder(@Nullable Folder parentFolder) {
        this.parent = parentFolder;
    }


    /**
     * Set the given path as new Path for the FSObject
     * @param path  new Path
     */
    @Override
    public void setPath(@NotNull Path path) {
        this.path = path;
    }

    /**
     * Returns the current path of the FSObject
     * @return current Path
     */
    @Override
    public Path getPath() {
        return path;
    }

    /**
     * Deletes the LocalFSObject itself from
     * the filetree and in the real Filesystem
     *
     */
    @Override
    public void delete() {
        //TODO Fehlerbehandlung
        Path path = getPath();
        if (path != null){
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
