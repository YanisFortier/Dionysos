package com.yfortier.dionysos

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.yfortier.dionysos.adapters.ParticipantAdapter
import com.yfortier.dionysos.composants.ParticipantItem
import java.text.SimpleDateFormat
import java.util.*


class CreerEvenementActivity : AppCompatActivity() {
	private val TAG = "CreerEvenementActivity"
	var firebase: FirebaseFirestore = FirebaseFirestore.getInstance()
	private val KEY_TITLE = "Nom"
	private val KEY_DESCRIPTION = "Description"
	private val KEY_DATE = "Date"
	private val KEY_PARTICIPANTS = "Participants"

	private lateinit var mRecyclerView: RecyclerView
	private lateinit var mAdapter: RecyclerView.Adapter<*>
	private lateinit var mLayoutManager: RecyclerView.LayoutManager
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_creer_evenement)
		setSupportActionBar(findViewById<MaterialToolbar>(R.id.topAppBar))
		val textInputDatePicker: TextInputEditText = findViewById(R.id.textInputDatePicker)
		val textInputNomEvenement = findViewById<TextInputEditText>(R.id.textInputNomEvenement)
		val textInputDescriptionEvenement = findViewById<TextInputEditText>(R.id.textInputDescriptionEvenement)
		val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)
		val boutonMenuAjouter = findViewById<MaterialToolbar>(R.id.topAppBar).menu.findItem(R.id.boutonMenuAjouter)

		gererDatePicker(textInputDatePicker)
		gererAffichageAjouterEvenement(textInputNomEvenement, textInputDescriptionEvenement, boutonMenuAjouter, chipGroup, textInputDatePicker)
		gererListeParticipants()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		super.onCreateOptionsMenu(menu)
		menuInflater.inflate(R.menu.menu_creer_evenement, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		Toast.makeText(this, "Prout !", Toast.LENGTH_LONG).show()
		if (item.itemId == R.id.boutonMenuAjouter) {
			ajouterEvenement()
			return true
		}
		return false
	}

	private fun ajouterEvenement() {
		val evenement: MutableMap<String, Any> = HashMap()
		evenement[KEY_TITLE] = findViewById<TextInputEditText>(R.id.textInputNomEvenement).text.toString()
		evenement[KEY_DESCRIPTION] = findViewById<TextInputEditText>(R.id.textInputDescriptionEvenement).text.toString()
		evenement[KEY_DATE] = findViewById<TextInputEditText>(R.id.textInputDatePicker).text.toString()
		Toast.makeText(this, "Click !", Toast.LENGTH_LONG).show()
		// Add a new document with a generated ID
		firebase.collection("evenements").add(evenement).addOnSuccessListener { p0 ->
			Log.e(TAG, "Evenement ajoute - ID: " + p0.id)
		}.addOnFailureListener { e ->
			Log.e(TAG, "Erreur lors de l'ajout de l'evenement", e)
		}
	}

	private fun gererListeParticipants() {
		val listeParticipants: ArrayList<ParticipantItem> = ArrayList()
		val boutonAjouterParticipant = findViewById<Button>(R.id.buttonAjouterParticipant)
		val textInputParticipant = findViewById<EditText>(R.id.textInputParticipant)
		boutonAjouterParticipant.setOnClickListener {
			if (textInputParticipant.text.toString().isNotEmpty()) {
				listeParticipants.add(ParticipantItem(textInputParticipant.text.toString()))
				textInputParticipant.hint = "Participant"
				textInputParticipant.text.clear()
				mAdapter.notifyItemInserted(listeParticipants.size)

			}

		}
		mRecyclerView = findViewById(R.id.recyclerView)
		mLayoutManager = LinearLayoutManager(this)
		mAdapter = ParticipantAdapter(listeParticipants)
		mRecyclerView.setHasFixedSize(false)
		mRecyclerView.layoutManager = mLayoutManager
		mRecyclerView.adapter = mAdapter
	}

	private fun gererAffichageAjouterEvenement(
			textInputNom: TextInputEditText,
			textInputDescription: TextInputEditText,
			boutonMenuAjouter: MenuItem,
			chipGroup: ChipGroup,
			textInputDatePicker: TextInputEditText,
	) {        // DatePicker
		textInputDatePicker.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {}
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				verifierSiEvenementValide(textInputNom, textInputDescription, boutonMenuAjouter, chipGroup, textInputDatePicker)
			}
		})        // ChipGroup
		chipGroup.setOnCheckedChangeListener { _, _ ->
			verifierSiEvenementValide(textInputNom, textInputDescription, boutonMenuAjouter, chipGroup, textInputDatePicker)
		}

		// Nom Evenement
		textInputNom.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {}
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				verifierSiEvenementValide(textInputNom, textInputDescription, boutonMenuAjouter, chipGroup, textInputDatePicker)
			}
		})

		// Description Evenement
		textInputDescription.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {}
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				verifierSiEvenementValide(textInputNom, textInputDescription, boutonMenuAjouter, chipGroup, textInputDatePicker)
			}
		})
	}

	private fun verifierSiEvenementValide(
			textInputNom: TextInputEditText,
			textInputDescription: TextInputEditText,
			boutonMenuAjouter: MenuItem,
			chipGroup: ChipGroup,
			textInputDatePicker: TextInputEditText,
	) {
		val categorieValide = estCategorieValide(chipGroup)
		val nomValide = textInputNom.text.toString().isNotEmpty()
		val dateValide = textInputDatePicker.text.toString().isNotEmpty()
		val descriptionValide = textInputDescription.text.toString().isNotEmpty()
		val estEvenementValide = nomValide && descriptionValide && categorieValide && dateValide
		boutonMenuAjouter.isVisible = estEvenementValide
	}

	private fun estCategorieValide(chipGroup: ChipGroup): Boolean {
		var categorieValide = false
		if (findViewById<Chip>(chipGroup.checkedChipId) != null) {
			categorieValide = findViewById<Chip>(chipGroup.checkedChipId).text.toString().isNotEmpty()
		}
		return categorieValide
	}

	private fun gererDatePicker(textInputDatePicker: TextInputEditText) {
		val calendrier: Calendar = Calendar.getInstance()

		val date = OnDateSetListener { _, year, month, day ->
			calendrier.set(Calendar.YEAR, year)
			calendrier.set(Calendar.MONTH, month)
			calendrier.set(Calendar.DAY_OF_MONTH, day)
			textInputDatePicker.setText(SimpleDateFormat("dd/MM/yy", Locale.FRANCE).format(calendrier.time))
		}

		textInputDatePicker.setOnClickListener {
			DatePickerDialog(this, date, calendrier.get(Calendar.YEAR), calendrier.get(Calendar.MONTH), calendrier.get(Calendar.DAY_OF_MONTH)).show()
		}
	}
}
