package com.ing.ingterior.ui.site.defects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.site.SiteActivity
import com.ing.ingterior.ui.viewmodel.SiteViewModel
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.button.VisualDotLineButton

class SiteDefectsFragment : Fragment() {
    companion object {
        private const val TAG = "SiteDefectsFragment"

        @JvmStatic
        fun newInstance(site: Site?) =
            SiteDefectsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SITE, site)
                }
            }
    }

    private lateinit var siteViewModel: SiteViewModel
    private lateinit var vdbAddDefect: VisualDotLineButton
    private lateinit var frameImageLayout: FrameLayout
    private lateinit var ivBluePrintView: ImageView
    private lateinit var markImageView: ImageView

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
        return inflater.inflate(R.layout.fragment_site_defects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewBinding(view)
    }

    private fun initViewBinding(view: View){
        frameImageLayout = view.findViewById(R.id.frame_site_blueprint_layout)
        frameImageLayout.post {
            val params = frameImageLayout.layoutParams
            params.height = frameImageLayout.width // 너비와 같게 설정
            frameImageLayout.layoutParams = params
        }
        ivBluePrintView = view.findViewById(R.id.iv_site_blueprint)
        vdbAddDefect = view.findViewById(R.id.vdb_site_add_defect)


        vdbAddDefect.setOnClickListener {
            if(siteViewModel.site == null) {
                Toast.makeText(requireContext(), "에러 발생", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
            else Factory.get().getMove().moveSiteInsertDefectActivity(requireActivity(), siteViewModel.site!!)
        }

        // 동적으로 마크 생성 및 추가
        markImageView = ImageView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.mark_size), resources.getDimensionPixelSize(R.dimen.mark_size)) // 마크의 크기 설정
            setImageResource(R.drawable.ic_mark) // 마크 이미지 설정
            x = resources.getDimensionPixelSize(R.dimen.popup_item_width).toFloat() // 초기 X 위치 설정
            y = resources.getDimensionPixelSize(R.dimen.popup_item_width).toFloat() // 초기 Y 위치 설정
        }
        frameImageLayout.addView(markImageView)

        (requireActivity() as SiteActivity).setTitleCompat(siteViewModel.site?.siteName ?: "")
        Glide.with(requireActivity()).load(siteViewModel.site?.imageLocation).into(ivBluePrintView)
    }
}