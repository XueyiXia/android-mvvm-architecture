package com.framework.http.config

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:57
 * @说明:
 */

class DownloadConfigure {

    private object Config {
        val downloadConfigure = DownloadConfigure()
    }

    companion object {
        fun get(): DownloadConfigure {
            return Config.downloadConfigure
        }
    }

    var directoryFile: String = ""
    var filename: String = ""
    var md5: String? = null
    var isBreakpoint = false
    var urlLink:String=""


    /**
     *
     * @param directoryFile String
     * @return DownloadConfigure
     */

    fun setDirectoryFile(directoryFile: String): DownloadConfigure {
        this.directoryFile = directoryFile
        return this
    }

    /**
     *
     * @return String
     */
//    fun getDirectoryFile()=this.directoryFile

    /**
     *
     * @param fileName String
     * @return DownloadConfigure
     */
    fun setFileName(fileName: String): DownloadConfigure {
        this.filename = fileName
        return this
    }


    /**
     *
     * @param urlLink String
     * @return DownloadConfigure
     */
    fun setDownloadUrl(urlLink: String): DownloadConfigure {
        this.urlLink = urlLink
        return this
    }

    /**
     *
     * @return String
     */
//    fun getFileName()=this.filename


    /**
     * 是否断点下载
     * @param breakpoint Boolean
     * @return DownloadConfigure
     */
    fun breakpoint(breakpoint: Boolean): DownloadConfigure {
        isBreakpoint = breakpoint
        return this
    }

    /**
     *
     * @return Boolean
     */
//    fun getIsBreakpoint()=this.isBreakpoint


    /**
     *
     * @param md5 String?
     * @return DownloadConfigure
     */
    fun setMd5(md5: String?): DownloadConfigure {
        this.md5 = md5
        return this
    }

    /**
     *
     * @return String?
     */
//    fun getMd5()=md5
}