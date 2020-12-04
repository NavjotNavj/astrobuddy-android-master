package `in`.appnow.astrobuddy.rest

import `in`.appnow.astrobuddy.utils.Logger
import android.content.Context
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * Created by sonu on 19:42, 08/06/18
 * Copyright (c) 2018 . All rights reserved.
 */

object SslUtils {

    fun getSslContextForCertificateFile(context: Context): SSLContext {
        try {
            val keyStore = SslUtils.getKeyStore(context)
            val sslContext = SSLContext.getInstance("SSL")
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())
            return sslContext
        } catch (e: Exception) {
            val msg = "Error during creating SslContext for certificate from assets"
            e.printStackTrace()
            throw RuntimeException(msg)
        }
    }

    private fun getKeyStore(context: Context): KeyStore? {
        var keyStore: KeyStore? = null
        try {
            val assetManager = context.assets
            val cf = CertificateFactory.getInstance("X.509")

            //to read string cert data
            //  val caInput = ByteArrayInputStream(fileName.toByteArray(Charsets.UTF_8))
            val certs = arrayOf("prod_server.crt","abtest_server.crt")
            val keyStoreType = KeyStore.getDefaultType()
            keyStore = KeyStore.getInstance(keyStoreType)
            keyStore!!.load(null, null)
            for (i in 1 until certs.size + 1) {
                val caInput = assetManager.open(certs[i - 1])
                Logger.DebugLog("SSSLL ", "FILE : " + certs[i - 1])

                val ca: Certificate
                try {
                    ca = cf.generateCertificate(caInput)
                    Logger.DebugLog("SslUtilsAndroid", "ca=" + (ca as X509Certificate).subjectDN)
                } finally {
                    caInput.close()
                }

                keyStore.setCertificateEntry("ca" + i, ca)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.DebugLog("SSL", e.localizedMessage)
        }

        return keyStore
    }


    fun getTrustAllHostsSSLSocketFactory(): SSLSocketFactory? {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager

            return sslContext.socketFactory
        } catch (e: KeyManagementException) {
            e.printStackTrace()
            return null
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

    }
}