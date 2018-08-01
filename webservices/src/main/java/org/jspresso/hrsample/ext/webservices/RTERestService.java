/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.hrsample.ext.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.framework.util.exception.NestedRuntimeException;

import org.jspresso.hrsample.ext.model.RTEMedia;

/**
 * A sample web service for rich text editor backend callbacks.
 *
 * @author Vincent Vandenschrick
 */
public class RTERestService extends AbstractService implements IRTERestService {

  @Override
  public TemplateDto[] getTemplates() {
    HibernateBackendController backendController = (HibernateBackendController) getBackendController();
    TemplateDto template = new TemplateDto();
    template.title = "Sample template";
    template.description = "Sample template description";
    template.content = "<b>Test Template</b> for " + backendController.getApplicationSession().getUsername();
    return new TemplateDto[]{template};
  }

  @Override
  public ImageLocationDto uploadImage(MultipartFormDataInput input) {
    String fileName = "unknown";
    Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
    List<InputPart> inputParts = uploadForm.get("file");
    byte[] content = null;

    // Only 1 part with the file
    InputPart inputPart = inputParts.get(0);
    try {
      MultivaluedMap<String, String> header = inputPart.getHeaders();
      String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
      for (String disposition : contentDisposition) {
        if ((disposition.trim().startsWith("filename"))) {
          String[] name = disposition.split("=");
          fileName = name[1].trim().replaceAll("\"", "");
        }
      }
      //convert the uploaded file to inputstream
      InputStream inputStream = inputPart.getBody(InputStream.class, null);
      content = IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      throw new NestedRuntimeException(e);
    }

    final IBackendController backendController = getBackendController();
    final RTEMedia media = backendController.getEntityFactory().createEntityInstance(RTEMedia.class);
    media.setName(fileName);
    media.setContent(content);
    backendController.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        backendController.registerForUpdate(backendController.cloneInUnitOfWork(media));
      }
    });
    ImageLocationDto mediaLocation = new ImageLocationDto();
    mediaLocation.location = getDownloadUrl(media);
    return mediaLocation;
  }

  @Override
  public Response downloadImage(String id) {
    final IBackendController backendController = getBackendController();
    Response.ResponseBuilder response;
    RTEMedia media = ((HibernateBackendController) backendController).findById(id, EMergeMode.MERGE_LAZY,
        RTEMedia.class);
    if (media != null && media.getContent() != null) {
      response = Response.ok(media.getContent());
      response.header("Content-Disposition", "attachment;filename=" + media.getName());
    } else {
      response = Response.status(Response.Status.NOT_FOUND);
    }
    return response.build();
  }

  @Override
  public ImageListElementDto[] listImages() {
    final IBackendController backendController = getBackendController();
    List<ImageListElementDto> mediaList = new ArrayList<>();
    List<RTEMedia> medias = ((HibernateBackendController) backendController).findByCriteria(
        EnhancedDetachedCriteria.forClass(RTEMedia.class), EMergeMode.MERGE_LAZY, RTEMedia.class);
    for (RTEMedia media : medias) {
      ImageListElementDto mediaListElt = new ImageListElementDto();
      mediaListElt.title = media.getName();
      mediaListElt.value = getDownloadUrl(media);
      mediaList.add(mediaListElt);
    }
    return mediaList.toArray(new ImageListElementDto[0]);
  }

  public String getDownloadUrl(RTEMedia media) {
    return "/rest/rte/image/" + media.getId();
  }

}
