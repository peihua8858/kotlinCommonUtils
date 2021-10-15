@file:JvmName("UriUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.annotation.Nullable
import com.socks.library.KLog
import java.io.File
import java.net.MalformedURLException
import java.net.URL

/**
 * 正则方式判断字符编码，默认为UTF-8
 *
 * @param uri
 * @return
 */
fun Uri?.isDecodeURL(): Boolean {
    return this?.toString()?.isDecodeURL() ?: false
}

/**
 * 比较Uri host 和path是否相等
 */
fun Uri?.compareUri(uri: Uri?): Boolean {
    return this?.path.equals(uri?.path)
            && this?.host.equals(uri?.host)
}

/**
 * 比较Uri host 和path是否相等
 */
fun Any?.compareUri(uri1: Uri, uri: Uri): Boolean {
    return uri1.path.equals(uri.path, ignoreCase = true)
            && uri1.host.equals(uri.host, ignoreCase = true)
}

/**
 * Convert android.net.Uri to java.net.URL as necessary for some networking APIs.
 *
 * @param uri uri to convert
 * @return load pointing to the same resource as uri
 */
@Nullable
fun Uri?.uriToUrl(): URL? {
    return if (this == null) {
        null
    } else try {
        URL(toString())
    } catch (e: MalformedURLException) {
        // This should never happen since we got a valid uri
        throw RuntimeException(e)
    }
}

/**
 * Check if uri represents network resource
 *
 * @param uri uri to check
 * @return true if uri's scheme is equal to "http" or "https"
 */
fun Uri?.isNetworkUri(): Boolean {
    val scheme = getSchemeOrNull()
    return HTTPS_SCHEME == scheme || HTTP_SCHEME == scheme
}

/**
 * Check if uri represents local file
 *
 * @param uri uri to check
 * @return true if uri's scheme is equal to "file"
 */
fun Uri?.isLocalFileUri(): Boolean {
    val scheme = getSchemeOrNull()
    return LOCAL_FILE_SCHEME == scheme
}

/**
 * Check if uri represents local content
 *
 * @param uri uri to check
 * @return true if uri's scheme is equal to "content"
 */
fun Uri?.isLocalContentUri(): Boolean {
    val scheme = getSchemeOrNull()
    return LOCAL_CONTENT_SCHEME == scheme
}

/**
 * Checks if the given URI is a general Contact URI, and not a specific display photo.
 *
 * @param uri the URI to check
 * @return true if the uri is a Contact URI, and is not already specifying a display photo.
 */
fun Uri?.isLocalContactUri(): Boolean {
    if (this == null) {
        return false
    }
    return (isLocalContentUri()
            && ContactsContract.AUTHORITY == authority &&
            path?.startsWith(LOCAL_CONTACT_IMAGE_URI?.path ?: "") == true)
}

/**
 * Checks if the given URI is for a photo from the device's local media store.
 *
 * @param uri the URI to check
 * @return true if the URI points to a media store photo
 */
fun Uri?.isLocalCameraUri(): Boolean {
    if (this == null) {
        return false
    }
    val uriString = toString()
    return (uriString.startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())
            || uriString.startsWith(MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString()))
}

/**
 * Check if uri represents local asset
 *
 * @param uri uri to check
 * @return true if uri's scheme is equal to "asset"
 */
fun Uri?.isLocalAssetUri(): Boolean {
    val scheme = getSchemeOrNull()
    return LOCAL_ASSET_SCHEME == scheme
}

/**
 * Check if uri represents fully qualified resource URI.
 *
 * @param uri uri to check
 * @return true if uri's scheme is equal to [.QUALIFIED_RESOURCE_SCHEME]
 */
fun Uri?.isQualifiedResourceUri(): Boolean {
    val scheme = getSchemeOrNull()
    return QUALIFIED_RESOURCE_SCHEME == scheme
}

/**
 * Check if the uri is a data uri
 */
fun Uri?.isDataUri(): Boolean {
    return DATA_SCHEME == getSchemeOrNull()
}

/**
 * @param uri uri to extract scheme from, possibly null
 * @return null if uri is null, result of uri.getScheme() otherwise
 */
@Nullable
fun Uri?.getSchemeOrNull(): String? {
    return this?.scheme
}

/**
 * A wrapper around [Uri.parse] that returns null if the input is null.
 *
 * @param uriAsString the uri as a string
 * @return the parsed Uri or null if the input was null
 */
fun String?.parseUriOrNull(): Uri? {
    return if (this != null) Uri.parse(this) else null
}

/**
 * Get the path of a file from the Uri.
 *
 * @param contentResolver the content resolver which will query for the source file
 * @param srcUri          The source uri
 * @return The Path for the file or null if doesn't exists
 */
@Nullable
fun Uri?.getRealPathFromUri(contentResolver: ContentResolver): String? {
    if (this == null) {
        return null
    }
    var result: String? = null
    if (isLocalContentUri()) {
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(this, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (idx != -1) {
                    result = cursor.getString(idx)
                }
            }
        } finally {
            cursor?.close()
        }
    } else if (isLocalFileUri()) {
        result = path
    }
    return result
}

/**
 * Returns a URI for a given file using [Uri.fromFile].
 *
 * @param file a file with a valid path
 * @return the URI
 */
fun File?.getUriForFile(): Uri? {
    if (this == null) {
        return null
    }
    return Uri.fromFile(this)
}

/**
 * Returns a URI for the given resource ID in the given package. Use this method only if you need
 * to specify a package name different to your application's main package.
 *
 * @param resourceId  to resource ID to use
 * @return the URI
 */
fun Int.getUriForResource(packageName: String): Uri {
    return Uri.Builder()
            .scheme(QUALIFIED_RESOURCE_SCHEME)
            .authority(packageName)
            .path(toString())
            .build()
}

/**
 * Returns a URI for the given resource ID in the given package. Use this method only if you need
 * to specify a package name different to your application's main package.
 *
 * @param packageName a package name (e.g. com.facebook.myapp.plugin)
 * @param resourceId  to resource ID to use
 * @return the URI
 */
fun Int.getUriForQualifiedResource(packageName: String): Uri {
    return Uri.Builder()
            .scheme(QUALIFIED_RESOURCE_SCHEME)
            .authority(packageName)
            .path(toString())
            .build()
}

fun Uri?.getRealPathFromURI(context: Context): String? {
    if (this == null) {
        return null
    }
    val result: String?
    val cursor = context.contentResolver.query(this, arrayOf(MediaStore.Images.ImageColumns.DATA),  //
            null, null, null)
    if (cursor == null) result = path else {
        cursor.moveToFirst()
        val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        result = cursor.getString(index)
        cursor.close()
    }
    return result
}

fun Context.getResourceUri(resourceId: Int): Uri? {
    return try {
        val resources: Resources = resources
        Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(resourceId) + '/'
                + resources.getResourceTypeName(resourceId) + '/'
                + resources.getResourceEntryName(resourceId))
    } catch (e: Exception) {
        KLog.w("Received invalid resource id: $this", e)
        null
    }
}

fun Int.getResourceUri(context: Context): Uri? {
    return context.getResourceUri(this)
}

/**
 * 获取Uri参数的异常处理
 *
 * @param uri 需要获取指定参数的uri
 * @param key 参数key值
 * @return
 * @author dingpeihua
 * @date 2016/5/28 17:46
 * @version 1.0
 */
fun getUriParameter(uri: Uri, key: String?): String {
    val parameter: String? = try {
        uri.getQueryParameter(key)
    } catch (e: Exception) {
        ""
    }
    return if (parameter.isNullOrEmpty()) "" else parameter
}

/**
 * http scheme for URIs
 */
const val HTTP_SCHEME = "http"
const val HTTPS_SCHEME = "https"

/**
 * File scheme for URIs
 */
const val LOCAL_FILE_SCHEME = "file"

/**
 * Content URI scheme for URIs
 */
const val LOCAL_CONTENT_SCHEME = "content"

/**
 * URI prefix (including scheme) for contact photos
 */
private val LOCAL_CONTACT_IMAGE_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "display_photo")

/**
 * Asset scheme for URIs
 */
const val LOCAL_ASSET_SCHEME = "asset"

/**
 * Resource scheme for fully qualified resources which might have a package name that is different
 * than the application one. This has the constant value of "android.resource".
 */
const val QUALIFIED_RESOURCE_SCHEME = ContentResolver.SCHEME_ANDROID_RESOURCE

/**
 * Data scheme for URIs
 */
const val DATA_SCHEME = "data"