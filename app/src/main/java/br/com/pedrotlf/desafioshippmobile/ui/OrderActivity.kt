package br.com.pedrotlf.desafioshippmobile.ui

import android.os.Bundle
import br.com.pedrotlf.desafioshippmobile.EstablishmentOrder
import br.com.pedrotlf.desafioshippmobile.OrderViewPagerAdapter
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.ui.*
import br.com.pedrotlf.desafioshippmobile.ui.establishment.EstablishmentOrderDescriptionFragment
import br.com.pedrotlf.desafioshippmobile.ui.establishment.EstablishmentOrderPriceFragment
import br.com.pedrotlf.desafioshippmobile.ui.establishment.EstablishmentOrderResumeFragment
import br.com.pedrotlf.desafioshippmobile.ui.establishment.EstablishmentsListFragment
import br.com.pedrotlf.desafioshippmobile.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.indeterminateProgressDialog

class OrderActivity : BaseActivity() {
//
//    private val establishmentsListFragment = EstablishmentsListFragment.getInstance {
//        establishmentOrder -> establishmentClicked(establishmentOrder)
//    }
//    private val establishmentOrderDescriptionFragment = EstablishmentOrderDescriptionFragment.getInstance{
//        establishmentOrder -> orderDetailsConfirmClicked(establishmentOrder)
//    }
//    private val establishmentOrderPriceFragment = EstablishmentOrderPriceFragment.getInstance{
//        establishmentOrder -> orderPriceConfirmClicked(establishmentOrder)
//    }
//    private val establishmentOrderResumeFragment = EstablishmentOrderResumeFragment.getInstance {
//        establishmentOrder -> orderResumeConfirmClicked(establishmentOrder)
//    }
//    private val orderSuccessFragment = OrderSuccessFragment.getInstance {
//            onBackPressed()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val fragList = listOf(
//                establishmentsListFragment,
//                establishmentOrderDescriptionFragment,
//                establishmentOrderPriceFragment,
//                establishmentOrderResumeFragment,
//                orderSuccessFragment
//        )
//        viewPager.adapter = OrderViewPagerAdapter(fragList, this)
//        viewPager.isUserInputEnabled = false
//    }
//
//    private fun establishmentClicked(establishmentOrder: EstablishmentOrder) {
//        establishmentOrderDescriptionFragment.setInfo(establishmentOrder)
//        pagerNextPage()
//    }
//
//    private fun orderDetailsConfirmClicked(establishmentOrder: EstablishmentOrder){
//        establishmentOrderPriceFragment.setInfo(establishmentOrder)
//        pagerNextPage()
//    }
//
//    private fun orderPriceConfirmClicked(establishmentOrder: EstablishmentOrder){
//        val progress = indeterminateProgressDialog(R.string.establishment_order_price_confirm_loading)
//        progress.setCancelable(false)
//        orderViewModel.validateResume(establishmentOrder) { resumeResponse->
//            progress.dismiss()
//            establishmentOrder.totalPrice = resumeResponse?.totalValue
//            establishmentOrderResumeFragment.setInfo(establishmentOrder)
//            pagerNextPage()
//        }
//    }
//
//    private fun orderResumeConfirmClicked(establishmentOrder: EstablishmentOrder){
//        val progress = indeterminateProgressDialog(R.string.establishment_order_price_confirm_loading)
//        progress.setCancelable(false)
//        orderViewModel.checkout(establishmentOrder) { checkoutResponse ->
//            progress.dismiss()
//            establishmentOrder.totalPrice = checkoutResponse?.value
//            val successMessage = checkoutResponse?.message
//            orderSuccessFragment.setInfo(establishmentOrder)
//            pagerNextPage()
//        }
//    }
//
//    private fun pagerNextPage(){
//        val currPos: Int = viewPager.currentItem
//        if ((currPos + 1) != viewPager.adapter?.itemCount) {
//            viewPager.currentItem = currPos + 1
//        }
//    }
//
//    private fun pagerPreviousPage(): Boolean{
//        val currPos: Int = viewPager.currentItem
//        if (currPos != 0) {
//            viewPager.currentItem = currPos - 1
//            return true
//        }
//        return false
//    }
//
//    override fun onBackPressed() {
//        if(viewPager.currentItem + 1 == viewPager.adapter?.itemCount) {
//            establishmentsListFragment.fragmentNeedsReloading = true
//            viewPager.currentItem = 0
//        }else if(!pagerPreviousPage())
//            super.onBackPressed()
//    }
}