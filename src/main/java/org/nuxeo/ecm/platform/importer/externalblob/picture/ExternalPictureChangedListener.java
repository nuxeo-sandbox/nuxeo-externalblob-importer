package org.nuxeo.ecm.platform.importer.externalblob.picture;

import static org.nuxeo.ecm.core.api.event.DocumentEventTypes.ABOUT_TO_CREATE;
import static org.nuxeo.ecm.platform.picture.api.ImagingDocumentConstants.PICTURE_FACET;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.picture.api.adapters.AbstractPictureAdapter;
import org.nuxeo.ecm.platform.picture.listener.PictureChangedListener;
import org.nuxeo.ecm.platform.picture.listener.PictureViewsGenerationListener;

public class ExternalPictureChangedListener extends PictureChangedListener {

    @Override
    public void handleEvent(Event event) throws ClientException {
        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }
        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel doc = docCtx.getSourceDocument();
        if (doc.hasFacet(PICTURE_FACET) && !doc.isProxy()) {
            Property fileProp = doc.getProperty("externalfile:content");
            Property viewsProp = doc.getProperty(AbstractPictureAdapter.VIEWS_PROPERTY);

            if (!viewsProp.isDirty() && (ABOUT_TO_CREATE.equals(event.getName()) || fileProp.isDirty())) {
                preFillPictureViews(docCtx.getCoreSession(), doc);
            } else {
                docCtx.setProperty(PictureViewsGenerationListener.DISABLE_PICTURE_VIEWS_GENERATION_LISTENER, true);
            }
        }
    }


}
