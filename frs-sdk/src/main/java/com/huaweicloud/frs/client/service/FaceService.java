package com.huaweicloud.frs.client.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.huaweicloud.frs.access.FrsAccess;
import com.huaweicloud.frs.client.param.AddExternalFields;
import com.huaweicloud.frs.client.result.AddFaceResult;
import com.huaweicloud.frs.client.result.DeleteFaceResult;
import com.huaweicloud.frs.client.result.GetFaceResult;
import com.huaweicloud.frs.common.FrsConstant;
import com.huaweicloud.frs.common.FrsException;
import com.huaweicloud.frs.common.ImageType;
import com.huaweicloud.frs.utils.HttpResponseUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Face operation service
 * Includes add, get and delete operations
 */
public class FaceService {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private FrsAccess service;
    private String projectId;

    /**
     * Construct face operation service, invoked by frs client
     *
     * @param service   frs access
     * @param projectId project id
     */
    FaceService(FrsAccess service, String projectId) {
        this.service = service;
        this.projectId = projectId;
    }

    private AddFaceResult addFace(String faceSetName, String externalImageId, String image, ImageType imageType, AddExternalFields addExternalFields) throws FrsException, IOException {
        String uri = String.format(FrsConstant.getFaceAddUri(), this.projectId, faceSetName);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> json = new HashMap<>();
        if (ImageType.BASE64 == imageType) {
            json.put("image_base64", image);
        } else {
            json.put("image_url", image);
        }
        if (null != externalImageId) {
            json.put("external_image_id", externalImageId);
        }
        if (null != addExternalFields) {
            json.put("external_fields", addExternalFields.getExternalFields());
        }

        RequestBody requestBody = RequestBody.create(JSON, mapper.writeValueAsString(json));
        Response httpResponse = this.service.post(uri, requestBody);
        return HttpResponseUtils.httpResponse2Result(httpResponse, AddFaceResult.class);
    }

    /**
     * Add face to face set by base64
     *
     * @param faceSetName       Face set name
     * @param externalImageId   External image id
     * @param imageBase64       Base64 of image
     * @param addExternalFields External fields of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByBase64(String faceSetName, String externalImageId, String imageBase64, AddExternalFields addExternalFields) throws FrsException, IOException {
        return this.addFace(faceSetName, externalImageId, imageBase64, ImageType.BASE64, addExternalFields);
    }

    /**
     * Add face to face set by base64
     *
     * @param faceSetName       Face set name
     * @param imageBase64       Base64 of image
     * @param addExternalFields External fields of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByBase64(String faceSetName, String imageBase64, AddExternalFields addExternalFields) throws FrsException, IOException {
        return this.addFaceByBase64(faceSetName, null, imageBase64, addExternalFields);
    }

    /**
     * Add face to face set by base64
     *
     * @param faceSetName     Face set name
     * @param externalImageId External image id
     * @param imageBase64     Base64 of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByBase64(String faceSetName, String externalImageId, String imageBase64) throws FrsException, IOException {
        return this.addFaceByBase64(faceSetName, externalImageId, imageBase64, null);
    }

    /**
     * Add face to face set by base64
     *
     * @param faceSetName Face set name
     * @param imageBase64 Base64 of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByBase64(String faceSetName, String imageBase64) throws FrsException, IOException {
        return this.addFaceByBase64(faceSetName, null, imageBase64, null);
    }

    /**
     * Add face to face set by file path
     *
     * @param faceSetName       Face set name
     * @param externalImageId   External image id
     * @param filePath          File path of image
     * @param addExternalFields External fields of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByFile(String faceSetName, String externalImageId, String filePath, AddExternalFields addExternalFields) throws FrsException, IOException {
        String uri = String.format(FrsConstant.getFaceAddUri(), this.projectId, faceSetName);
        File image = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        RequestBody imageBody = RequestBody.create(MediaType.parse("application/octet-stream"), image);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("image_file", image.getName(), imageBody);
        if (externalImageId != null) {
            builder.addFormDataPart("external_image_id", externalImageId);
        }
        if (addExternalFields != null) {
            builder.addFormDataPart("external_fields", mapper.writeValueAsString(addExternalFields.getExternalFields()));
        }
        RequestBody requestBody = builder.build();
        Response httpResponse = this.service.post(uri, requestBody);
        return HttpResponseUtils.httpResponse2Result(httpResponse, AddFaceResult.class);
    }

    /**
     * Add face to face set by file path
     *
     * @param faceSetName       Face set name
     * @param filePath          File path of image
     * @param addExternalFields External fields of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByFile(String faceSetName, String filePath, AddExternalFields addExternalFields) throws FrsException, IOException {
        return this.addFaceByFile(faceSetName, null, filePath, addExternalFields);
    }

    /**
     * Add face to face set by file path
     *
     * @param faceSetName     Face set name
     * @param externalImageId External image id
     * @param filePath        File path of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByFile(String faceSetName, String externalImageId, String filePath) throws FrsException, IOException {
        return this.addFaceByFile(faceSetName, externalImageId, filePath, null);
    }

    /**
     * Add face to face set by file path
     *
     * @param faceSetName Face set name
     * @param filePath    File path of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByFile(String faceSetName, String filePath) throws FrsException, IOException {
        return this.addFaceByFile(faceSetName, null, filePath, null);
    }

    /**
     * @param faceSetName       Face set name
     * @param externalImageId   External image id
     * @param obsUrl            Obs url of image
     * @param addExternalFields External fields of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByObsUrl(String faceSetName, String externalImageId, String obsUrl, AddExternalFields addExternalFields) throws FrsException, IOException {
        return this.addFace(faceSetName, externalImageId, obsUrl, ImageType.OBSURL, addExternalFields);
    }

    /**
     * @param faceSetName       Face set name
     * @param obsUrl            Obs url of image
     * @param addExternalFields External fields of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByObsUrl(String faceSetName, String obsUrl, AddExternalFields addExternalFields) throws FrsException, IOException {
        return this.addFaceByObsUrl(faceSetName, null, obsUrl, addExternalFields);
    }

    /**
     * @param faceSetName     Face set name
     * @param externalImageId External image id
     * @param obsUrl          Obs url of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByObsUrl(String faceSetName, String externalImageId, String obsUrl) throws FrsException, IOException {
        return this.addFaceByObsUrl(faceSetName, externalImageId, obsUrl, null);
    }

    /**
     * @param faceSetName Face set name
     * @param obsUrl      Obs url of image
     * @return Result of add face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public AddFaceResult addFaceByObsUrl(String faceSetName, String obsUrl) throws FrsException, IOException {
        return this.addFaceByObsUrl(faceSetName, null, obsUrl, null);
    }

    /**
     * @param faceSetName Face set name
     * @param offset      Offset in face set
     * @param limit       Number of face returned
     * @return Result of get faces
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public GetFaceResult getFaces(String faceSetName, int offset, int limit) throws FrsException, IOException {
        String uri = String.format(FrsConstant.getFaceGetRangeUri(), this.projectId, faceSetName, offset, limit);
        Response httpResponse = this.service.get(uri);
        return HttpResponseUtils.httpResponse2Result(httpResponse, GetFaceResult.class);
    }

    /**
     * @param faceSetName Face set name
     * @param faceId      Face id
     * @return Result of get face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public GetFaceResult getFace(String faceSetName, String faceId) throws FrsException, IOException {
        String uri = String.format(FrsConstant.getFaceGetOneUri(), this.projectId, faceSetName, faceId);
        Response httpResponse = this.service.get(uri);
        return HttpResponseUtils.httpResponse2Result(httpResponse, GetFaceResult.class);
    }

    /**
     * @param faceSetName Face set name
     * @param faceId      Face id
     * @return Result of delete face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public DeleteFaceResult deleteFaceByFaceId(String faceSetName, String faceId) throws FrsException, IOException {
        String uri = String.format(FrsConstant.getFaceDeleteByFaceIdUri(), this.projectId, faceSetName, faceId);
        Response httpResponse = this.service.delete(uri);
        return HttpResponseUtils.httpResponse2Result(httpResponse, DeleteFaceResult.class);
    }

    /**
     * @param faceSetName     Face set name
     * @param externalImageId External image id
     * @return Result of delete face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public DeleteFaceResult deleteFaceByExternalImageId(String faceSetName, String externalImageId) throws FrsException, IOException {
        String uri = String.format(FrsConstant.getFaceDeleteByExternalImageIdUri(), this.projectId, faceSetName, externalImageId);
        Response httpResponse = this.service.delete(uri);
        return HttpResponseUtils.httpResponse2Result(httpResponse, DeleteFaceResult.class);
    }

    /**
     * @param faceSetName Face set name
     * @param fieldId     Field id
     * @param fieldValue  Field value
     * @return Result of delete face
     * @throws FrsException Throws while http status code is not 200
     * @throws IOException  IO exception
     */
    public DeleteFaceResult deleteFaceByFieldId(String faceSetName, String fieldId, String fieldValue) throws FrsException, IOException {
        String uri = String.format(FrsConstant.getFaceDeleteByFieldIdUri(), this.projectId, faceSetName, fieldId, fieldValue);
        Response httpResponse = this.service.delete(uri);
        return HttpResponseUtils.httpResponse2Result(httpResponse, DeleteFaceResult.class);
    }
}
