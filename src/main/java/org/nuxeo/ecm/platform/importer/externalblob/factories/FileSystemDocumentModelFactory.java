package org.nuxeo.ecm.platform.importer.externalblob.factories;

import java.io.File;
import java.io.IOException;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.blob.BlobManager;
import org.nuxeo.ecm.core.blob.FilesystemBlobProvider;
import org.nuxeo.ecm.core.blob.BlobManager.BlobInfo;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.ecm.platform.importer.factories.DefaultDocumentModelFactory;
import org.nuxeo.ecm.platform.importer.source.SourceNode;
import org.nuxeo.runtime.api.Framework;

public class FileSystemDocumentModelFactory extends DefaultDocumentModelFactory {

    public FileSystemDocumentModelFactory() {
        super();

    }

    public FileSystemDocumentModelFactory(String folderishType, String leafType) {
        super(folderishType, leafType);
    }

    @Override
    public DocumentModel createLeafNode(CoreSession session,
            DocumentModel parent, SourceNode node) throws IOException {


        File file = node.getBlobHolder().getBlob().getFile();

        BlobInfo blobInfo = new BlobInfo();
        blobInfo.key = file.getAbsolutePath();

        Blob blob = ((FilesystemBlobProvider)Framework.getService(BlobManager.class).getBlobProvider("fs"))
                .createBlob(blobInfo);

        DocumentModel doc = Framework.getLocalService(FileManager.class)
                .createDocumentFromBlob(session, blob, parent.getPathAsString(), true, file.getName());

        return doc;
    }



}
