/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 *
 * $Id$
 */

package org.nuxeo.ecm.platform.importer.externalblob.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.importer.executor.DefaultImporterExecutor;
import org.nuxeo.ecm.platform.importer.externalblob.factories.ExternalBlobDocumentModelFactory;
import org.nuxeo.ecm.platform.importer.source.FileSourceNode;
import org.nuxeo.ecm.platform.importer.source.SourceNode;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({"org.nuxeo.ecm.platform.content.template",
    "org.nuxeo.ecm.platform.picture.core",
    "org.nuxeo.ecm.platform.picture.api",
    "org.nuxeo.ecm.platform.video.core"})
@LocalDeploy("org.nuxeo.ecm.platform.importer.externalblob.test:test-importer-externalblob-contrib.xml")
public class ExternalVideoImporterTest {

	@Inject
    protected CoreSession session;

    @Test
    public void testExternalImport() throws Exception {

    	File source = FileUtils.getResourceFileFromContext("import-vids");
    	//System.out.println(source.getAbsolutePath());

        SourceNode src = new FileSourceNode(source);
        //System.out.println(src.getSourcePath());

        String targetPath = "/default-domain/workspaces/";

        DefaultImporterExecutor executor = new DefaultImporterExecutor();

        executor.setFactory(new ExternalBlobDocumentModelFactory());

        executor.run(src, targetPath, false, 10, 5, true);

        long createdDocs = executor.getCreatedDocsCounter();
        assertTrue(createdDocs > 0);

        session.save();

      DocumentModelList list = session.query("SELECT * FROM DOCUMENT order by ecm:path");
      for (DocumentModel documentModel : list) {
          System.out.println(documentModel.getPathAsString());
      }

        DocumentModel doc = session.getDocument(new PathRef(targetPath + "import-vids/Sunset.mov"));

        Blob blob = (FileBlob)doc.getProperty("externalfile", "content");
        assertNotNull(blob);
        File file = blob.getFile();
        assertEquals(file.getName(), "Sunset.mov");
        System.out.println("File path -"+file.getAbsolutePath());

        BlobHolder bh1 = doc.getAdapter(BlobHolder.class);
        assertNotNull(bh1);
        System.out.println("BlobHolder filepath - "+bh1.getFilePath());
        System.out.println("BlobHolder instance of - "+bh1.getClass());

        Blob extBlob = bh1.getBlob();
        assertNotNull(extBlob);
        System.out.println("Blob filepath - "+extBlob.getFile().getAbsolutePath());

    }

}
