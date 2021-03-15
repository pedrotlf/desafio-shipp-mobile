package br.com.pedrotlf.desafioshippmobile

import android.os.Bundle
import br.com.pedrotlf.desafioshippmobile.establishments.*
import br.com.pedrotlf.desafioshippmobile.order.EstablishmentOrderDescriptionFragment
import br.com.pedrotlf.desafioshippmobile.order.EstablishmentOrderPriceFragment
import br.com.pedrotlf.desafioshippmobile.order.EstablishmentOrderResumeFragment
import br.com.pedrotlf.desafioshippmobile.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.indeterminateProgressDialog

class OrderActivity : BaseActivity() {

    private val establishmentsListFragment = EstablishmentsListFragment.getInstance {
        establishmentOrder -> establishmentClicked(establishmentOrder)
    }
    private val establishmentOrderDescriptionFragment = EstablishmentOrderDescriptionFragment.getInstance{
        establishmentOrder -> orderDetailsConfirmClicked(establishmentOrder)
    }
    private val establishmentOrderPriceFragment = EstablishmentOrderPriceFragment.getInstance{
        establishmentOrder -> orderPriceConfirmClicked(establishmentOrder)
    }
    private val establishmentOrderResumeFragment = EstablishmentOrderResumeFragment.getInstance {
        establishmentOrder ->
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragList = listOf(
                establishmentsListFragment,
                establishmentOrderDescriptionFragment,
                establishmentOrderPriceFragment,
                establishmentOrderResumeFragment
        )
        viewPager.adapter = OrderViewPagerAdapter(fragList, this)
        viewPager.isUserInputEnabled = false
    }

    private fun establishmentClicked(establishmentOrder: EstablishmentOrder) {
        establishmentOrderDescriptionFragment.setInfo(establishmentOrder)
        pagerNextPage()
    }

    private fun orderDetailsConfirmClicked(establishmentOrder: EstablishmentOrder){
        establishmentOrderPriceFragment.setInfo(establishmentOrder)
        pagerNextPage()
    }

    private fun orderPriceConfirmClicked(establishmentOrder: EstablishmentOrder){
        val progress = indeterminateProgressDialog(R.string.establishment_order_price_confirm_loading)
        progress.setCancelable(false)
        orderViewModel.validateResume(establishmentOrder) { resumeResponse->
            progress.dismiss()
            establishmentOrder.totalPrice = resumeResponse?.totalValue
            establishmentOrderResumeFragment.setInfo(establishmentOrder)
            pagerNextPage()
        }
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