package br.gov.rs.ddpa_seapdr.aquasaude.tilapia.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.gov.rs.ddpa_seapdr.aquasaude.tilapia.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.otaliastudios.cameraview.Gesture
import com.otaliastudios.cameraview.GestureAction
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseCameraActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var sheetBehavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sheetBehavior = BottomSheetBehavior.from(bottomLayout)
        sheetBehavior.isHideable = true
        cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        cameraFrame.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }

}