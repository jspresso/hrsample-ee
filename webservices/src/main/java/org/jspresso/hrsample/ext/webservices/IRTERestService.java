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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.fileupload.FileItem;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * Employee REST service specification. Can be used on client side by RestEasy
 * client framework.
 *
 * @author Vincent Vandenschrick
 */
@Path("/rte")
public interface IRTERestService {

  @GET
  @Path("/templates")
  @Produces({MediaType.APPLICATION_JSON})
  TemplateDto[] getTemplates();

  @POST
  @Path("/uploadImage")
  @Consumes("multipart/form-data")
  @Produces({MediaType.APPLICATION_JSON})
  ImageLocationDto uploadImage(MultipartFormDataInput input);

  @GET
  @Path("/image/{id}")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  Response downloadImage(@PathParam("id") String id);

  @GET
  @Path("/listImages")
  @Produces({MediaType.APPLICATION_JSON})
  ImageListElementDto[] listImages();

  /**
   * The type Template dto.
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  class TemplateDto {

    /**
     * The Title.
     */
    public String title;

    /**
     * The Description.
     */
    public String description;

    /**
     * The Content.
     */
    public String content;
  }

  /**
   * The type Image location dto.
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  class ImageLocationDto {

    /**
     * The Location.
     */
    public String location;
  }

  /**
   * The type Image location dto.
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  class ImageListElementDto {

    /**
     * The Title.
     */
    public String title;

    /**
     * The Value.
     */
    public String value;
  }
}
