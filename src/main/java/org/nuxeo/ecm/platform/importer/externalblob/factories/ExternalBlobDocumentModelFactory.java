/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *     Thibaud Arguillere (Nuxeo)
 *
 * $Id$
 */

package org.nuxeo.ecm.platform.importer.externalblob.factories;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.platform.importer.factories.DefaultDocumentModelFactory;
import org.nuxeo.ecm.platform.importer.source.SourceNode;

/**
 * External Blob referenced DocumentFactory
 *
 *
 * @author Mike Obrebski
 */
public class ExternalBlobDocumentModelFactory extends DefaultDocumentModelFactory {

    /**
     * Instantiate a DefaultDocumentModelFactory that creates Folder and File
     */
    public ExternalBlobDocumentModelFactory() {
        this("Folder", "File");
    }

    /**
     * Instantiate a DefaultDocumentModelFactory that creates specified types doc
     *
     * @param folderishType the folderish type
     * @param leafType the other type
     */
    public ExternalBlobDocumentModelFactory(String folderishType, String leafType) {
        this.folderishType = folderishType;
        this.leafType = leafType;
    }

    /*
     * (non-Javadoc)
     * @seeorg.nuxeo.ecm.platform.importer.base.ImporterDocumentModelFactory#
     * createLeafNode(org.nuxeo.ecm.core.api.CoreSession, org.nuxeo.ecm.core.api.DocumentModel,
     * org.nuxeo.ecm.platform.importer.base.SourceNode)
     */
    @Override
    public DocumentModel createLeafNode(CoreSession session, DocumentModel parent, SourceNode node) throws IOException {

    	BlobHolder bh = node.getBlobHolder();
        String leafTypeToUse = getDocTypeToUse(bh);
        if (leafTypeToUse == null) {
            leafTypeToUse = leafType;
        }
        List<String> facets = getFacetsToUse(bh);

        String mimeType = bh.getBlob().getMimeType();
        if (mimeType == null) {
            mimeType = getMimeType(node.getName());
        }

        String name = getValidNameFromFileName(node.getName());
        String fileName = node.getName();

        //TODO Use mimeType / FileManager to create appropriate Doc Type

        DocumentModel doc = session.createDocumentModel(parent.getPathAsString(), name, leafTypeToUse);
        for (String facet : facets) {
            doc.addFacet(facet);
        }

        Blob extBlob = bh.getBlob();

        doc.setProperty("dublincore", "title", fileName);
        doc.setProperty("externalfile", "filename", fileName);


        //Set Map directly
        Map<String, Serializable> extContent = new HashMap<String, Serializable>();
        extContent.put("mime-type", mimeType);
        extContent.put("uri", "fs:"+node.getSourcePath());
        extContent.put("length", extBlob.getLength());
        extContent.put("name", fileName);
        extContent.put("digest", bh.getHash());

        doc.setProperty("externalfile", "content", extContent);

        doc = session.createDocument(doc);

        if (bh != null) {
            doc = setDocumentProperties(session, bh.getProperties(), doc);
        }

        return doc;
    }

}
