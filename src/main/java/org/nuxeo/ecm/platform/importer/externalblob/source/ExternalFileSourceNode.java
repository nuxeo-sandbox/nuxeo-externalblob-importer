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
 *
 * $Id$
 */

package org.nuxeo.ecm.platform.importer.externalblob.source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.platform.importer.source.SourceNode;

/**
 * Simple Filesystem based {@link SourceNode}
 *
 * @author Mike Obrebski
 */
public class ExternalFileSourceNode implements SourceNode {

    protected File file;

    public ExternalFileSourceNode(File file) {
        this.file = file;
    }

    public ExternalFileSourceNode(String path) {
    	
        this.file = new File(path);
    }

    public BlobHolder getBlobHolder() throws IOException {
    	System.out.println("File: "+file.getAbsolutePath());
        return new SimpleBlobHolder(Blobs.createBlob(file));
    }

    public List<SourceNode> getChildren() throws IOException {

        List<SourceNode> children = new ArrayList<SourceNode>();

        for (File child : file.listFiles()) {
            children.add(new ExternalFileSourceNode(child));
        }
        return children;
    }

    public boolean isFolderish() {
        return file.isDirectory();
    }

    public String getName() {
        return file.getName();
    }

    public String getSourcePath() {
        return file.getAbsolutePath();
    }

    public File getFile() {
        return file;
    }

    public static String getFileNameNoExt(File file) {
        String name = file.getName();
        int p = name.lastIndexOf('.');
        if (p == -1) {
            return name;
        }
        return name.substring(0, p);
    }

}
