package ma.projet.soapclient.ws

import ma.projet.soapclient.beans.Compte
import ma.projet.soapclient.beans.TypeCompte
import org.ksoap2.SoapEnvelope
import org.ksoap2.SoapFault
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.text.SimpleDateFormat
import java.util.*

fun SoapObject.getPropertySafelyAsString(name: String): String? {
    return if (hasProperty(name)) getProperty(name)?.toString() else null
}

class Service {
    private val NAMESPACE = "http://ws.tp_13.example.com/"
    private val URL = "http://10.0.2.2:8082/services/ws"
    private val METHOD_GET_COMPTES = "getComptes"
    private val METHOD_CREATE_COMPTE = "createCompte"
    private val METHOD_DELETE_COMPTE = "deleteCompte"

    /**
    * Récupère la liste des comptes via le service SOAP.
    */
    fun getComptes(): List<Compte> {
        val request = SoapObject(NAMESPACE, METHOD_GET_COMPTES)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11).apply {
            dotNet = false
            setOutputSoapObject(request)
        }
        val transport = HttpTransportSE(URL)
        val comptes = mutableListOf<Compte>()

        try {
            transport.call("", envelope)

            // Check if response is a SoapFault
            if (envelope.bodyIn is SoapFault) {
                val fault = envelope.bodyIn as SoapFault
                throw Exception("SOAP Fault: ${fault.faultstring}")
            }

            val response = envelope.bodyIn as? SoapObject
                ?: throw Exception("Invalid response from server")

            for (i in 0 until response.propertyCount) {
                val soapCompte = response.getProperty(i) as SoapObject

                val dateStr = soapCompte.getPropertySafelyAsString("dateCreation")
                val date = dateStr?.let { SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(it) } ?: Date()

                val typeStr = soapCompte.getPropertySafelyAsString("type")
                val type = typeStr?.let { TypeCompte.valueOf(it) } ?: TypeCompte.COURANT

                val compte = Compte(
                    id = soapCompte.getPropertySafelyAsString("id")?.toLongOrNull(),
                    solde = soapCompte.getPropertySafelyAsString("solde")?.toDoubleOrNull() ?: 0.0,
                    dateCreation = date,
                    type = type
                )
                comptes.add(compte)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

        return comptes
    }

    /**
    * Crée un nouveau compte via le service SOAP.
    * @param solde Solde initial du compte.
    * @param type Type du compte (COURANT ou EPARGNE).
    */
    fun createCompte(solde: Double, type: TypeCompte): Boolean {
        val request = SoapObject(NAMESPACE, METHOD_CREATE_COMPTE)

        // Add solde property
        request.addProperty("solde", solde.toString())

        // Add type property
        request.addProperty("type", type.name)

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11).apply {
            dotNet = false
            setOutputSoapObject(request)
        }
        val transport = HttpTransportSE(URL)

        return try {
            transport.call("", envelope)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
    * Supprime un compte en fonction de son ID via le service SOAP.
    * @param id Identifiant du compte à supprimer.
    */
    fun deleteCompte(id: Long): Boolean {
        val request = SoapObject(NAMESPACE, METHOD_DELETE_COMPTE)
        request.addProperty("id", id.toString())

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11).apply {
            dotNet = false
            setOutputSoapObject(request)
        }
        val transport = HttpTransportSE(URL)

        return try {
            transport.call("", envelope)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}