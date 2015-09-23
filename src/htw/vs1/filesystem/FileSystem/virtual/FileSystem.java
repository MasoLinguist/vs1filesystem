package htw.vs1.filesystem.FileSystem.virtual;

import com.sun.istack.internal.NotNull;
import htw.vs1.filesystem.FileSystem.exceptions.*;
import htw.vs1.filesystem.Network.Discovery.DiscoveryManager;
import htw.vs1.filesystem.Network.Discovery.FileSystemServer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@link FileSystem} represents a file system on which you
 * can perform actions like:
 * <ul>
 *     <li>print the current Working directory: {@link #printWorkingDirectory()}</li>
 *     <li>change the working directory {@link #changeDirectory(String)}</li>
 *     <li>list the content of the current folder {@link #listDirectoryContent()}</li>
 *     <li>{@link #rename(String, String)} / {@link #delete(String)}</li>
 * </ul>
 *
 * Created by felix on 03.06.15.
 */
public class FileSystem implements FileSystemInterface {

    /**
     * String-Definition to change current working
     * directory to the parent folder.
     */
    private static final String UP = "..";

    private static final String THIS_FOLDER = ".";

    private final boolean mountAllowed;

    /**
     * The folder to evaluate a absolute
     * path from.
     */
    private Folder rootFolder;

    /**
     * Current working folder.
     */
    private Folder workingFolder;

    public FileSystem() {
        this(false);
    }

    public FileSystem(boolean mountAllowed) {
        this(LocalFolder.getRootFolder(), mountAllowed);
    }

    /**
     * Creates a new FileSystem with the given {@link Folder}
     * as working directory.
     *
     * @param workingFolder {@link Folder} to work on.
     */
    public FileSystem(Folder workingFolder, boolean mountAllowed) {
        if (mountAllowed) {
            try {
                rootFolder = new LocalFolder("");
                rootFolder.add(workingFolder);
                this.workingFolder = rootFolder;
            } catch (FileSystemException e) {
                e.printStackTrace();
            }
        } else {
            this.workingFolder = workingFolder;
            this.rootFolder = workingFolder;
        }
        this.mountAllowed = mountAllowed;
    }

    @Override
    public void startDiscoveryListener(boolean start) {
        if (start) {
            DiscoveryManager.getInstance().startListener();
        } else {
            DiscoveryManager.getInstance().stopListener();
        }
    }

    @Override
    public Collection<FileSystemServer> listAvailableFileSystemServers() {
        return DiscoveryManager.getInstance().getDiscoveredServers();
    }

    /**
     * {@inheritDoc}
     */
    public void setWorkingDirectory(Folder workingFolder) {
        this.workingFolder = workingFolder;
    }

    /**
     * Gets the current working directory.
     *
     * @return the current working directory.
     */
    @Override
    public Folder getWorkingDirectory() {
        return workingFolder;
    }

    @Override
    public void createFSObject(String name, boolean isFolder) throws FileSystemException {
        getWorkingDirectory().add(name, isFolder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeDirectory(@NotNull String path) throws FileSystemException {
        path = path.replace("\\", "/");
        if (!path.isEmpty() && path.substring(0,1).equals("/")) {
            // path is an absolute path
            setWorkingDirectory(rootFolder);
        }

        String[] pathArray = path.split("/");
        for (String name : pathArray) {
            if (name.equals(rootFolder.getName()) || name.isEmpty()) {
                continue;
            }
            changeDirectoryToSubFolder(name);
        }
    }

    private void changeDirectoryToSubFolder(@NotNull String name) throws FileSystemException {
        FSObject o;
        switch (name) {
            case UP:
                o = workingFolder.getParentFolder();
                break;
            case THIS_FOLDER:
                // stay in this folder
                return;
            default:
                o = workingFolder.getObject(name);
                break;
        }

        if (null == o) {
            // if your are in the root folder then there is nothing to do.
            // unix does not show any error message either.
            return;
        }

        if (o instanceof RemoteFolder) {
            ((RemoteFolder) o).changeDir();
        }

        if (o instanceof Folder) {
            workingFolder = (Folder) o;
        } else {
            throw new ObjectNotFoundException(name, FSObjectException.OBJECTNOTFOUND);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FSObject>  listDirectoryContent() throws FileSystemException {
        return workingFolder.getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String printWorkingDirectory() {
        String pwd = workingFolder.getAbsolutePath();
        if (!pwd.equals("/")) {
            pwd = pwd.substring(0, pwd.length()-1);
        }
        return pwd;
    }

    /**
     * Searchs recursively through the current working directory
     * all {@link FSObject} with the given name.
     *
     * @param name name of the {@link FSObject} to search for.
     * @return a {@link List} of {@link FSObject}s matching to the given name.
     */
    @Override
    public List<FSObject> search(String name) {
        LinkedList<FSObject> result = new LinkedList<>();
        workingFolder.search(result, name);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
     public void rename(@NotNull String name, String newName) throws FileSystemException {
        FSObject toRename = workingFolder.getObject(name);
        toRename.setName(newName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(@NotNull String name) throws FileSystemException {
        workingFolder.delete(name);
    }

    /**
     * Mounts a {@link RemoteFolder} into our file system.
     *
     * @param name Name of the new remote foler
     * @param remoteIP   IP-Adress of the remote file system
     * @param remotePort Port of the remote file system
     * @param user       username
     * @param pass       password
     */
    @Override
    public void mount(String name, String remoteIP, int remotePort, String user, String pass) throws FileSystemException {
        if (!mountAllowed) {
            return;
        }

        rootFolder.add(new RemoteFolder(name, remoteIP, remotePort, user, pass));
    }
}
