package org.nuxeo.ecm.platform.importer.externalblob.factories;

import java.util.Set;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.BlobHolderFactory;
import org.nuxeo.ecm.core.api.blobholder.DocumentBlobHolder;
import org.nuxeo.ecm.platform.picture.api.adapters.PictureBlobHolder;

public class ExternalBlobHolderFactory implements BlobHolderFactory {

    @Override
    public BlobHolder getBlobHolder(DocumentModel doc) {
        BlobHolder blobHolder;

        Set<String> facets = doc.getFacets();

        if (facets.contains("externalfile") && !facets.contains("Picture")) {
            blobHolder = new DocumentBlobHolder(doc, "externalfile:content", "externalfile:filename");
        } else if (facets.contains("Picture")){
            blobHolder = new PictureBlobHolder(doc, "externalfile:content");
        } else {
            blobHolder = null;
        }
        return blobHolder;
    }

}
