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
import java.util.Calendar;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.api.model.impl.primitives.ExternalBlobProperty;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.importer.executor.DefaultImporterExecutor;
import org.nuxeo.ecm.platform.importer.externalblob.factories.ExternalBlobDocumentModelFactory;
import org.nuxeo.ecm.platform.importer.externalblob.source.ExternalFileSourceNode;
import org.nuxeo.ecm.platform.importer.source.FileSourceNode;
import org.nuxeo.ecm.platform.importer.source.FileWithMetadataSourceNode;
import org.nuxeo.ecm.platform.importer.source.RandomTextSourceNode;
import org.nuxeo.ecm.platform.importer.source.SourceNode;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy("org.nuxeo.ecm.platform.content.template")
@LocalDeploy("org.nuxeo.ecm.platform.importer.externalblob.test:test-importer-externalblob-contrib.xml")
public class ExternalBlobImporterTest {

	@Inject
    protected CoreSession session;

    @Test
    public void testExternalImportBasic() throws Exception {

    	File source = FileUtils.getResourceFileFromContext("import-src");
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
        DocumentModel doc1 = session.getDocument(new PathRef(targetPath + "import-src/hello.pdf"));
        
        assertNotNull(doc1);
        assertEquals(doc1.getType(), "ExternalFile");
        assertNotNull(doc1.getPropertyValue("externalfile:filename"));
        System.out.println("filename -"+doc1.getPropertyValue("externalfile:filename"));
        
        assertNotNull(doc1.getProperties("externalfile"));
        System.out.println("extfile -"+doc1.getProperties("externalfile").toString());
        //ExternalBlobProperty exProp = (ExternalBlobProperty)doc1.getProperty("externalfile", "content");
        System.out.println("extfile:content -"+doc1.getProperty("externalfile", "content").toString());
        
        Blob blob = (FileBlob)doc1.getProperty("externalfile", "content");
        
        assertNotNull(blob);
        File file = blob.getFile();
        assertEquals(file.getName(), "hello.pdf");
        
        //BlobHolder bh1=doc1.getAdapter(BlobHolder.class); //NULL
        //System.out.println(bh1.getFilePath());
        
//        Blob extBlob = blobHolder.getBlob();
//        assertNotNull(extBlob);
        
//        assertEquals("src1", doc1.getPropertyValue("dc:source").toString());
//
//        String[] subjects = (String[]) doc1.getPropertyValue("dc:subjects");
//        assertNotNull(subjects);
//        assertEquals("subject1", subjects[0]);
//        assertEquals("subject2", subjects[1]);
//        assertTrue(subjects.length == 2);
//
//        assertEquals(2008, ((Calendar) (doc1.getPropertyValue("dc:issued"))).get(Calendar.YEAR));
//
//        DocumentModel doc2 = session.getDocument(new PathRef(targetPath + "import-src/branch1/hello.pdf"));
//        assertEquals("src1", doc2.getPropertyValue("dc:source").toString());
//        subjects = (String[]) doc2.getPropertyValue("dc:subjects");
//        assertNotNull(subjects);
//        assertEquals("subject4", subjects[0]);
//        assertEquals("subject5", subjects[1]);
//        assertTrue(subjects.length == 2);
//
//        DocumentModel doc3 = session.getDocument(new PathRef(targetPath + "import-src/branch1/branch11/hello.pdf"));
//        assertEquals("src1", doc3.getPropertyValue("dc:source").toString());
//        subjects = (String[]) doc3.getPropertyValue("dc:subjects");
//        assertNotNull(subjects);
//        assertEquals("subject4", subjects[0]);
//        assertEquals("subject5", subjects[1]);
//        assertTrue(subjects.length == 2);
//
//        DocumentModel doc4 = session.getDocument(new PathRef(targetPath + "import-src/branch2/hello.pdf"));
//        assertEquals("src1", doc4.getPropertyValue("dc:source").toString());
//        subjects = (String[]) doc4.getPropertyValue("dc:subjects");
//        assertNotNull(subjects);
//        assertEquals("subject1", subjects[0]);
//        assertEquals("subject2", subjects[1]);
//        assertTrue(subjects.length == 2);
//
//        DocumentModel doc5 = session.getDocument(new PathRef(targetPath + "import-src/branch2/branch21/hello.pdf"));
//        assertEquals("src2", doc5.getPropertyValue("dc:source").toString());
//        subjects = (String[]) doc5.getPropertyValue("dc:subjects");
//        assertNotNull(subjects);
//        assertEquals("subject3", subjects[0]);
//        assertEquals("subject4", subjects[1]);
//        assertTrue(subjects.length == 2);
    }

}
