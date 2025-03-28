package com.framework.http.api

import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 15:40
 * @说明:
 */
interface APIService {


    /**
     * GET 请求
     * @param url String pi接口url
     * @param parameter MutableMap<String, Any> 请求参数map
     * @param header MutableMap<String, Any>  请求头map
     * @return Observable<JsonElement>
     */
    @GET
    fun get(
        @Url url: String,
        @QueryMap parameter: MutableMap<String, Any>,
        @HeaderMap header: MutableMap<String, Any>
    ):Observable<JsonElement>


    /**
     *
     * @param url String
     * @param parameter Map<String?, Any?>
     * @param header Map<String?, Any?>
     * @return Observable<JsonElement>
     */
    @FormUrlEncoded
    @POST
    fun post(
        @Url url: String,
        @FieldMap parameter: Map<String, Any>,
        @HeaderMap header: Map<String, Any>
    ): Observable<JsonElement>


    /**
     * @param requestBody 用于String/JSON格式数据
     */
    @POST
    fun post(
        @Url url: String,
        @Body requestBody: RequestBody,
        @HeaderMap header: Map<String, Any>
    ): Observable<JsonElement>


    /**
     * DELETE 请求
     *
     * @param url       api接口url
     * @param parameter 请求参数map
     * @param header    请求头map
     * @return
     */
    @DELETE
    @JvmSuppressWildcards
    fun delete(
        @Url url: String,
        @QueryMap parameter: Map<String, Any>,
        @HeaderMap header: Map<String, Any>
    ): Observable<JsonElement>

    @HTTP(method = "DELETE", hasBody = true)
    fun delete(@Url url: String): Observable<JsonElement>

    /**
     * PUT 请求
     *
     * @param url       api接口url
     * @param parameter 请求参数map
     * @param header    请求头map
     * @return
     */
    @FormUrlEncoded
    @PUT
    fun put(
        @Url url: String,
        @FieldMap parameter: Map<String, Any>,
        @HeaderMap header: Map<String, Any>
    ): Observable<JsonElement>


    /**
     * 多文件上传
     *
     * @param url       api接口url
     * @param parameter 请求接口参数
     * @param header    请求头map
     * @param fileList  文件列表
     * @return
     * @Multipart 文件上传注解 multipart/form-data
     */
    @Multipart
    @POST
    @JvmSuppressWildcards
    fun upload(
        @Url url: String,
        @PartMap parameter: Map<String, Any>,
        @HeaderMap header: Map<String, Any>,
        @Part fileList: List<MultipartBody.Part>
    ): Observable<JsonElement>

    /**
     * 下载请求,普通下载
     * @param url
     * @return
     */
    @GET
    @Streaming
    fun download(@Url url: String): Observable<ResponseBody>

    /**
     * 断点续传下载
     *
     * @param range  断点下载范围 bytes= start - end
     * @param url    下载地址
     * @param header 请求头map
     * @return
     * @Streaming 防止内容写入内存, 大文件通过此注解避免OOM
     */
    @Streaming
    @GET
    fun download(@Url url: String,@Header("RANGE") range: String): Observable<ResponseBody>


    /**
     * HEAD请求
     * @param url
     * @return
     */
    @HEAD
    fun head(@Url url: String): Call<Void>
}