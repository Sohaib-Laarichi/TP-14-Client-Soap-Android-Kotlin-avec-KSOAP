package ma.projet.soapclient

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ma.projet.soapclient.adapter.CompteAdapter
import ma.projet.soapclient.beans.TypeCompte
import ma.projet.soapclient.ws.Service

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button
    private val adapter = CompteAdapter()
    private val service = Service()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupRecyclerView()
        setupListeners()
        loadComptes()
    }
    
    /**
    * Initialise les vues.
    */
    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        btnAdd = findViewById(R.id.fabAdd)
    }
    
    /**
    * Configure le RecyclerView.
    */
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        


        // Listener pour le bouton Supprimer
        adapter.onDeleteClick = { compte ->
            MaterialAlertDialogBuilder(this)
            .setTitle("Supprimer le compte")
            .setMessage("Voulez-vous vraiment supprimer ce compte ?")
            .setPositiveButton("Supprimer") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val success = service.deleteCompte(compte.id!!)
                    withContext(Dispatchers.Main) {
                        if (success) {
                            adapter.removeCompte(compte)
                            Toast.makeText(this@MainActivity, "Compte supprimé.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Erreur lors de la suppression.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
        }
    }
    
    /**
    * Configure les listeners.
    */
    private fun setupListeners() {
        btnAdd.setOnClickListener { showAddCompteDialog() }
    }
    
    /**
    * Affiche la boîte de dialogue pour ajouter un compte.
    */
    private fun showAddCompteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.popup, null)

        MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setTitle("Nouveau compte")
            .setPositiveButton("Ajouter") { _, _ ->
                val etSolde = dialogView.findViewById<TextInputEditText>(R.id.etSolde)
                val radioCourant = dialogView.findViewById<RadioButton>(R.id.radioCourant)

                val solde = etSolde.text.toString().toDoubleOrNull() ?: 0.0
                val type = if (radioCourant.isChecked) TypeCompte.COURANT else TypeCompte.EPARGNE

                lifecycleScope.launch(Dispatchers.IO) {
                    val success = service.createCompte(solde, type)
                    withContext(Dispatchers.Main) {
                        if (success) {
                            Toast.makeText(this@MainActivity, "Compte ajouté.", Toast.LENGTH_SHORT).show()
                            loadComptes()
                        } else {
                            Toast.makeText(this@MainActivity, "Erreur lors de l'ajout.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    /**
    * Charge la liste des comptes depuis le service SOAP.
    */
    private fun loadComptes() {
        println("loadComptes: Début du chargement...")
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val comptes = service.getComptes()
                println("loadComptes: ${comptes.size} comptes récupérés")
                comptes.forEach { compte ->
                    println("  - Compte #${compte.id}: ${compte.solde} DH, ${compte.type}")
                }
                withContext(Dispatchers.Main) {
                    if (comptes.isNotEmpty()) {
                        adapter.updateComptes(comptes)
                        println("loadComptes: Liste mise à jour dans l'adaptateur")
                    } else {
                        Toast.makeText(this@MainActivity, "Aucun compte trouvé.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                println("loadComptes: ERREUR - ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}