package br.gov.rs.ddpa_seapdr.aquasaude.tilapia.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.gov.rs.ddpa_seapdr.aquasaude.tilapia.R
import br.gov.rs.ddpa_seapdr.aquasaude.tilapia.HandleFileUpload
import br.gov.rs.ddpa_seapdr.aquasaude.tilapia.activity.isUserSignedIn
import br.gov.rs.ddpa_seapdr.aquasaude.tilapia.activity.tilapiaArray
import br.gov.rs.ddpa_seapdr.aquasaude.tilapia.activity.startAuth
import br.gov.rs.ddpa_seapdr.aquasaude.tilapia.model.Tilapia
import kotlinx.android.synthetic.main.item_row.view.*

class TilapiaAdater(private var tilapiaList: List<Tilapia>, private val handleFileUpload: HandleFileUpload) : androidx.recyclerview.widget.RecyclerView.Adapter<TilapiaAdater.TilapiaHolder>() {

    private lateinit var context: Context

    class TilapiaHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TilapiaHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return TilapiaHolder(view)
    }

    override fun getItemCount() = tilapiaList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TilapiaHolder, position: Int) {

        val currentItem = tilapiaList[position]

        when {
            currentItem.accuracy > .70 -> holder.itemView.itemAccuracy.setTextColor(ContextCompat.getColor(context, R.color.green))
            currentItem.accuracy < .30 -> holder.itemView.itemAccuracy.setTextColor(ContextCompat.getColor(context, R.color.red))
            else -> holder.itemView.itemAccuracy.setTextColor(ContextCompat.getColor(context, R.color.orange))
        }

        with(holder.itemView) {
            itemName.text = currentItem.name
            itemAccuracy.text = "Probability : ${(currentItem.accuracy * 100).toInt()}%"
            btnYes.setOnClickListener {
                if (isUserSignedIn())
                    handleFileUpload.uploadImageToStorage(currentItem.name)
                else
                    startAuth(handleFileUpload as AppCompatActivity)
            }
            btnNo.setOnClickListener {
                showPokemonSpinner()
            }
        }
    }

    fun setList(list: List<Tilapia>) {
        tilapiaList = list
        notifyDataSetChanged()
    }

    private fun showPokemonSpinner() {
        val tilapiaSpinnerAdapter = ArrayAdapter(context,
            R.layout.support_simple_spinner_dropdown_item, tilapiaArray)
        tilapiaSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val view = LayoutInflater.from(context).inflate(R.layout.tilapia_spinner_dialog, null, false);
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        spinner.adapter = tilapiaSpinnerAdapter

        val dialog = AlertDialog.Builder(context)
            .setTitle("Help us in making the app better")
            .setMessage("Select correct pokemon from the list below")
            .setView(view)
            .setPositiveButton("Submit") { dialog, _ ->
                handleFileUpload.uploadImageToStorage(spinner.selectedItem as String)
                dialog.cancel()
            }
            .create()
        dialog.show()
    }

}