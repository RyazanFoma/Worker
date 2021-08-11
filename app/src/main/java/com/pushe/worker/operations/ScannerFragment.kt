package com.pushe.worker.operations

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import com.pushe.worker.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScannerFragment : Fragment() {

    val args: ScannerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_scanner, container, false)

        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setTimeout(60000)
        integrator.setBeepEnabled(true)
        integrator.setPrompt(getString(R.string.scanner_operation))
        integrator.initiateScan()

        return rootView
    }

    /**
     * Callback for barcode scanner after clicking on fab button
     * @param requestCode - see {@link #IntentIntegrator.REQUEST_CODE}
     * @param resultCode - ignore
     * @param data - contains barcode
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result!=null) {
            if (result.contents == null) { //Canceled
                val originalIntent = result.originalIntent
                if (originalIntent == null) {
                    Log.d("ScannerFragment", "Cancelled scan")
                    Toast.makeText(activity, R.string.scanner_canceled, Toast.LENGTH_LONG).show()
                } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                    Log.d("ScannerFragment", "Cancelled scan due to missing camera permission")
                    Toast.makeText(activity, R.string.no_camera_permission, Toast.LENGTH_LONG).show()
                }
                findNavController().navigate(ScannerFragmentDirections.actionScannerToList(
                    userId = args.userId))
            } else { //Successful
                Log.d("OperationActivity", "Scanned")
                Toast.makeText(activity,
                    getString(R.string.scanner_successful)+result.contents,
                    Toast.LENGTH_LONG).show()
                findNavController().navigate(ScannerFragmentDirections.actionScannerToSummary(
                    userId = args.userId, result.contents))
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ScannerFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(/*param1: String, param2: String*/) =
//            ScannerFragment()
////            ScannerFragment().apply {
////                arguments = Bundle().apply {
////                    putString(ARG_PARAM1, param1)
////                    putString(ARG_PARAM2, param2)
////                }
////            }
//    }
}