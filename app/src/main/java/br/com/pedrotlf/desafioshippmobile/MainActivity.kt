package br.com.pedrotlf.desafioshippmobile

import android.graphics.Bitmap
import android.os.Bundle
import br.com.pedrotlf.desafioshippmobile.establishments.EstablishmentOrder
import br.com.pedrotlf.desafioshippmobile.establishments.EstablishmentOrderDescriptionFragment
import br.com.pedrotlf.desafioshippmobile.establishments.EstablishmentOrderPriceFragment
import br.com.pedrotlf.desafioshippmobile.establishments.EstablishmentsListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val establishmentsListFragment = EstablishmentsListFragment.getInstance {
        establishmentOrder -> establishmentClicked(establishmentOrder)
    }
    private val establishmentOrderDescriptionFragment = EstablishmentOrderDescriptionFragment.getInstance{
        establishmentOrder -> orderDetailsConfirmClicked(establishmentOrder)
    }
    private val establishmentOrderPriceFragment = EstablishmentOrderPriceFragment.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragList = listOf(
                establishmentsListFragment,
                establishmentOrderDescriptionFragment,
                establishmentOrderPriceFragment
        )
        viewPager.adapter = ViewPagerAdapter(fragList, this)
        viewPager.isUserInputEnabled = false

        if(checkLocationPermission())
            requestLocationPermission()
    }

    private fun establishmentClicked(establishmentOrder: EstablishmentOrder) {
        establishmentOrderDescriptionFragment.setInfo(establishmentOrder)
        pagerNextPage()
    }

    private fun orderDetailsConfirmClicked(establishmentOrder: EstablishmentOrder){
        establishmentOrderPriceFragment.setInfo(establishmentOrder)
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