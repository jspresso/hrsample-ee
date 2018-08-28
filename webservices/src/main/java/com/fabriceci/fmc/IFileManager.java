package com.fabriceci.fmc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;

import com.fabriceci.fmc.error.FileManagerException;
import com.fabriceci.fmc.model.FileData;
import com.fabriceci.fmc.model.InitiateData;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public interface IFileManager {

  String handleGetHeadRequest(String mode, HttpServletRequest request, HttpServletResponse response);

  String handlePostMultipartRequest(String mode, MultipartFormDataInput input, HttpServletRequest request,
                                    HttpServletResponse response);

  String handlePostFormUrlEncodedRequest(String mode, MultivaluedMap<String, String> input,
                                         HttpServletRequest request, HttpServletResponse response);

  InitiateData actionInitiate() throws FileManagerException;

  FileData actionGetInfo(String path) throws FileManagerException;

  List<FileData> actionReadFolder(String path, String type) throws FileManagerException;

  FileData actionMove(String sourcePath, String targetPath) throws FileManagerException;

  FileData actionDelete(String path) throws FileManagerException;

  FileData actionAddFolder(String path, String name) throws FileManagerException;

  FileData actionGetImage(HttpServletResponse response, String path, Boolean thumbnail) throws FileManagerException;

  // TO test :

  Object actionSeekFolder(String folderPath, String term) throws FileManagerException;

  FileData actionCopy(String sourcePath, String targetPath) throws FileManagerException;

  FileData actionRename(String sourcePath, String targetPath) throws FileManagerException;

  FileData actionReadFile(HttpServletRequest request, HttpServletResponse response, String path)
      throws FileManagerException;

  FileData actionSummarize() throws FileManagerException;

  FileData actionDownload(HttpServletResponse response, String path) throws FileManagerException;

  List<FileData> actionUpload(MultipartFormDataInput input, HttpServletRequest request, String path)
      throws FileManagerException;

  FileData actionSaveFile(String pathParam, String contentParam) throws FileManagerException;

  List<FileData> actionExtract(String sourcePath, String targetPath) throws FileManagerException;


}
