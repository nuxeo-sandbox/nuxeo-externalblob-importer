package org.nuxeo.ecm.platform.importer.externalblob.video;

import static org.nuxeo.ecm.core.api.event.DocumentEventTypes.BEFORE_DOC_UPDATE;
import static org.nuxeo.ecm.core.api.event.DocumentEventTypes.DOCUMENT_CREATED;
import static org.nuxeo.ecm.platform.picture.api.ImagingDocumentConstants.PICTURE_VIEWS_PROPERTY;
import static org.nuxeo.ecm.platform.video.VideoConstants.HAS_VIDEO_PREVIEW_FACET;
import static org.nuxeo.ecm.platform.video.VideoConstants.STORYBOARD_PROPERTY;
import static org.nuxeo.ecm.platform.video.VideoConstants.TRANSCODED_VIDEOS_PROPERTY;
import static org.nuxeo.ecm.platform.video.VideoConstants.VIDEO_CHANGED_EVENT;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.video.listener.VideoChangedListener;
import org.nuxeo.runtime.api.Framework;

public class MultiSourceVideoChangedListener extends VideoChangedListener {

    @Override
    public void handleEvent(Event event) throws ClientException {
        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }
        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel doc = docCtx.getSourceDocument();
        if (doc.hasFacet(HAS_VIDEO_PREVIEW_FACET) && !doc.isProxy()) {

            //Check if externally stored file content and set schema
            Property origVideoProperty = null;
            if (doc.hasFacet("externalfile")) {
                origVideoProperty = doc.getProperty("externalfile:content");
            } else {
                origVideoProperty = doc.getProperty("file:content");
            }
            if (DOCUMENT_CREATED.equals(event.getName()) || origVideoProperty.isDirty()) {

                Blob video = (Blob) origVideoProperty.getValue();
                updateVideoInfo(doc, video);

                if (BEFORE_DOC_UPDATE.equals(event.getName())) {
                    doc.setPropertyValue(TRANSCODED_VIDEOS_PROPERTY, null);
                    doc.setPropertyValue(STORYBOARD_PROPERTY, null);
                    doc.setPropertyValue(PICTURE_VIEWS_PROPERTY, null);
                }

                // only trigger the event if we really have a video
                if (video != null) {
                    Event trigger = docCtx.newEvent(VIDEO_CHANGED_EVENT);
                    EventService eventService = Framework.getLocalService(EventService.class);
                    eventService.fireEvent(trigger);
                }
            }
        }
    }


}
