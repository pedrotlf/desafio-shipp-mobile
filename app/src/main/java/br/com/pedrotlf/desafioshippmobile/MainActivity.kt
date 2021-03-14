package br.com.pedrotlf.desafioshippmobile

import android.graphics.Bitmap
import android.os.Bundle
import br.com.pedrotlf.desafioshippmobile.estabelecimentos.EstablishmentOrderDescriptionFragment
import br.com.pedrotlf.desafioshippmobile.estabelecimentos.EstablishmentsListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val establishmentsListFragment = EstablishmentsListFragment.getInstance { name, address, city, photo -> establishmentClicked(name, address, city, photo)}
    private val establishmentOrderDescriptionFragment = EstablishmentOrderDescriptionFragment.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragList = listOf(establishmentsListFragment, establishmentOrderDescriptionFragment)
        viewPager.adapter = ViewPagerAdapter(fragList, this)
        viewPager.isUserInputEnabled = false

        if(checkLocationPermission())
            requestLocationPermission()
    }

    private fun establishmentClicked(name: String, address: String, city: String?, photo: Bitmap?) {
        establishmentOrderDescriptionFragment.setInfo(name, address, city, photo)
        pagerNextPage()
    }

    private fun pagerNextPage(){
        val currPos: Int = viewPager.currentItem
        if ((currPos + 1) != viewPager.adapter?.itemCount) {
            viewPager.currentItem = currPos + 1
        }
    }

    private fun pagerPreviousPage(): Boolean{
        val currPos: Int = viewPager.currentItem
        if (currPos != 0) {
            viewPager.currentItem = currPos - 1
            return true
        }
        return false
    }

    override fun onBackPressed() {
        if(!pagerPreviousPage())
            super.onBackPressed()
    }
}