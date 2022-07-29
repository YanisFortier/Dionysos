package com.yfortier.dionysos

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.FirebaseFirestore
import com.yfortier.dionysos.databinding.ActivityChoixBinding


class ChoixActivity : AppCompatActivity() {
	private lateinit var binding: ActivityChoixBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_choix)
		binding = ActivityChoixBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.fabChoix.setOnClickListener {
			startActivity(Intent(applicationContext, CreerEvenementActivity::class.java))
		}

		// Read from the database
		val firebase: FirebaseFirestore = FirebaseFirestore.getInstance()
		firebase.collection("evenements").get().addOnSuccessListener { result ->
				val mainLinearLayout = binding.layoutCards
				mainLinearLayout.setPadding(16, 16, 16, 16)
				for (evenement in result) {
					var nomEvenement = "${evenement.data.get("Nom")}"
					var descEvenement = "${evenement.data.get("Description")}"
					var categorieEvenement = "${evenement.data.get("Categorie")}"

                    nomEvenement = nomEvenement.substring(0,1).uppercase() + nomEvenement.substring(1).lowercase();
                    descEvenement = descEvenement.substring(0,1).uppercase() + descEvenement.substring(1).lowercase();

                    val cardLinearLayout = LinearLayout(this)
					cardLinearLayout.orientation = LinearLayout.VERTICAL
                    val cardView = creerCardView()
                    val textViewTitre = creerNomCardView(nomEvenement)
                    val textViewDescription = creerDescriptionCardView(descEvenement,  categorieEvenement)

                    cardLinearLayout.addView(textViewTitre)
					cardLinearLayout.addView(textViewDescription)

					cardView.addView(cardLinearLayout)
					mainLinearLayout.addView(cardView)
				}
			}.addOnFailureListener { exception ->
				Log.w("ChoixActivity", "Error getting documents.", exception)
			}
	}

    private fun creerCardView(): CardView {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val cardView = MaterialCardView(this)
        cardView.radius = 15f
        cardView.setContentPadding(36, 36, 36, 36)
        cardView.layoutParams = layoutParams
        cardView.useCompatPadding = true
		cardView.strokeColor = Color.WHITE
        return cardView
    }

    private fun creerDescriptionCardView(descEvenement: String, categorieEvenement: String): TextView {
	    val split = categorieEvenement.split(" ")
	    val textViewDescription = MaterialTextView(this)
        textViewDescription.text = split[0] + " " + descEvenement
        textViewDescription.textSize = 16f
	    textViewDescription.setTextColor(Color.WHITE)
	    textViewDescription.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)


        return textViewDescription
    }

    private fun creerNomCardView(nomEvenement: String): TextView {
        val textViewNom = MaterialTextView(this)
        textViewNom.text = nomEvenement
        textViewNom.textSize = 24f
        textViewNom.setTextColor(Color.WHITE)
        textViewNom.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

	    return textViewNom
    }
}