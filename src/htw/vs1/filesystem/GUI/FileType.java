package htw.vs1.filesystem.GUI;

import htw.vs1.filesystem.FileSystem.virtual.Permissions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * This class is part of the package htw.vs1.filesystem.FileSystem.GUI and project ver1
 * Created by Marc Otting on 18.09.2015.
 * This class provides the following function(s):
 */
public class FileType {



    private final SimpleObjectProperty<Image> icon;
    private final SimpleObjectProperty<FileObject> fileName;
    private final SimpleStringProperty fileType;

    private Permissions permissions;

    private String fileNameString;

    public FileType (String fileName, boolean isFolder, Permissions permissions) {
        this.fileNameString = fileName;
        this.permissions = permissions;
        this.fileName = new SimpleObjectProperty<>(new FileObject(fileName, isFolder));
        this.fileType = new SimpleStringProperty(isFolder ? "Folder" : "File");
        this.icon = new SimpleObjectProperty<>(Resources.fsObjectIcon(isFolder, permissions));

    }

    public SimpleObjectProperty<FileObject> fileNameProperty() {
        return fileName;
    }

    public SimpleObjectProperty<Image> iconProperty() {
        return icon;
    }

    public String getFileType() {
        return fileType.get();
    }

    public SimpleStringProperty fileTypeProperty() {
        return fileType;
    }



    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(fileType.get());
        return buffer.toString();
    }

    public Permissions getPermissions() {
        return permissions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileType) {
            // two FileType-Objects are equal if they have the same fileNameString.
            return Objects.equals(((FileType) obj).fileNameString, fileNameString);
        }

        return super.equals(obj);
    }
}
