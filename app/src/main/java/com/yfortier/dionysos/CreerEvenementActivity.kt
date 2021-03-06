package com.yfortier.dionysos

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.yfortier.dionysos.transverse.constante.DionysosConstante
import java.text.SimpleDateFormat
import java.util.*


class CreerEvenementActivity : AppCompatActivity() {
	// FindViewById
	private lateinit var textInputDatePicker: TextInputEditText
	private lateinit var textInputNomEvenement: TextInputEditText
	private lateinit var textInputParticipant: TextInputEditText
	private lateinit var textInputDescriptionEvenement: TextInputEditText
	private lateinit var chipGroup: ChipGroup
	private lateinit var chipGroupParticipants: ChipGroup
	private lateinit var boutonMenuAjouter: MenuItem
	private lateinit var boutonAjouterParticipant: MaterialButton

	private val listeParticipants: ArrayList<String> = ArrayList()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_creer_evenement)
		textInputDatePicker = findViewById(R.id.textInputDatePicker)
		textInputNomEvenement = findViewById(R.id.textInputNomEvenement)
		textInputParticipant = findViewById(R.id.textInputParticipant)
		textInputDescriptionEvenement = findViewById(R.id.textInputDescriptionEvenement)
		boutonAjouterParticipant = findViewById(R.id.boutonAjouterParticipant)
		chipGroup = findViewById(R.id.chipGroup)
		chipGroupParticipants = findViewById(R.id.chipGroupParticipants)
		boutonMenuAjouter = findViewById<MaterialToolbar>(R.id.topAppBar).menu.findItem(R.id.boutonMenuAjouter)
		boutonMenuAjouter.isVisible = false

		gererDatePicker()
		gererAffichageAjouterEvenement()
		activerAjoutParticipant()
	}

	private fun activerSuppressionParticipants(chip: Chip) {
		chip.setOnCloseIconClickListener {
			var index = 0
			for (children in chipGroupParticipants.children) {
				if (children.id == chip.id) {
					listeParticipants.removeAt(index)
					chipGroupParticipants.removeView(it)
					verifierSiEvenementValide()
				}
				index++
			}
		}
	}

	private fun activerAjoutParticipant() {
		this.boutonAjouterParticipant.setOnClickListener {
			this.chipGroupParticipants.removeView(findViewById(R.id.chipOriginal))
			val prenomParticipant = textInputParticipant.text.toString().trim()
			if (prenomParticipant.isNotEmpty()) {
				val chip = Chip(this)
				val emojiRandom = this.resources.getStringArray(R.array.emojis).random()
				chip.text = getString(R.string.ajout_participant, emojiRandom, prenomParticipant)
				chip.isCloseIconVisible = true

				activerSuppressionParticipants(chip)
				textInputParticipant.setText("")
				textInputParticipant.hint = "Participant"

				this.chipGroupParticipants.addView(chip)
				listeParticipants.add(prenomParticipant)
				verifierSiEvenementValide()
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.boutonMenuAjouter) {
			ajouterEvenement(item)
			return true
		}
		return false
	}

	fun ajouterEvenement(item: MenuItem) {
		val firebase: FirebaseFirestore = FirebaseFirestore.getInstance()
		val evenement: MutableMap<String, Any> = HashMap()
		evenement[DionysosConstante.KEY_NOM] = textInputNomEvenement.text.toString()
		evenement[DionysosConstante.KEY_DESCRIPTION] = textInputDescriptionEvenement.text.toString()
		evenement[DionysosConstante.KEY_DATE] = textInputDatePicker.text.toString()
		evenement[DionysosConstante.KEY_CATEGORIE] = findViewById<Chip>(chipGroup.checkedChipId).text.toString()
		evenement[DionysosConstante.KEY_PARTICIPANTS] = listeParticipants

		firebase.collection("evenements").add(evenement).addOnSuccessListener { nouvelEvenement ->
			Toast.makeText(this, "Evenement ajoute - ID : " + nouvelEvenement.id, Toast.LENGTH_LONG).show()
			startActivity(Intent(applicationContext, ChoixActivity::class.java))
		}.addOnFailureListener {
			Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_LONG).show()
		}
	}


	private fun gererAffichageAjouterEvenement() {        // DatePicker
		textInputDatePicker.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {}
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				verifierSiEvenementValide()
			}
		})

		// ChipGroup
		chipGroup.setOnCheckedChangeListener { _, _ ->
			verifierSiEvenementValide()
		}

		// Nom Evenement
		textInputNomEvenement.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {}
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				verifierSiEvenementValide()
			}
		})

		// Description Evenement
		textInputDescriptionEvenement.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {}
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				verifierSiEvenementValide()
			}
		})
	}

	private fun verifierSiEvenementValide() {
		val categorieValide = estCategorieValide()
		val nomValide = textInputNomEvenement.text.toString().isNotEmpty()
		val dateValide = textInputDatePicker.text.toString().isNotEmpty()
		val descriptionValide = textInputDescriptionEvenement.text.toString().isNotEmpty()
		val listeParticipantValide = listeParticipants.isNotEmpty()
		val estEvenementValide = nomValide && descriptionValide && categorieValide && dateValide && listeParticipantValide
		boutonMenuAjouter.isVisible = estEvenementValide
	}

	private fun estCategorieValide(): Boolean {
		var categorieValide = false
		if (findViewById<Chip>(chipGroup.checkedChipId) != null) {
			categorieValide = findViewById<Chip>(chipGroup.checkedChipId).text.toString().isNotEmpty()
		}
		return categorieValide
	}

	private fun gererDatePicker() {
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
