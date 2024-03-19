package com.ing.ingterior.ui.site.management

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.ColorListAdapter
import com.ing.ingterior.ui.GridSpacingItemDecoration
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.site.SiteActivity
import com.ing.ingterior.ui.site.defects.SiteDefectsFragment
import com.ing.ingterior.ui.viewmodel.SiteViewModel
import com.ing.ingterior.util.getDisplayPixelSize
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.button.VisualDotLineButton

class SiteManagementFragment : Fragment() {

    companion object {
        private const val TAG = "SiteDefectsFragment"

        @JvmStatic
        fun newInstance(site: Site?) =
            SiteManagementFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SITE, site)
                }
            }
    }

    private lateinit var siteViewModel: SiteViewModel
    private lateinit var vdbAddManagement: VisualDotLineButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        siteViewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[SiteViewModel::class.java]
        arguments?.let {
            siteViewModel.site = it.getParcelableCompat<Site>(EXTRA_SITE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_site_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewBinding(view)

    }

    private fun initViewBinding(view: View){
        vdbAddManagement = view.findViewById(R.id.vdb_site_add_management)

        vdbAddManagement.setOnClickListener {
            if(siteViewModel.site == null) {
                Toast.makeText(requireContext(), "에러 발생", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
            else Factory.get().getMove().moveSiteInsertManagementActivity(requireActivity(), siteViewModel.site!!)
        }

    }

}