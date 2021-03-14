package br.com.pedrotlf.desafioshippmobile

import android.os.Bundle
import br.com.pedrotlf.desafioshippmobile.estabelecimentos.EstablishmentOrderDescriptionFragment
import br.com.pedrotlf.desafioshippmobile.estabelecimentos.EstablishmentsListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragList = listOf(EstablishmentsListFragment(), EstablishmentOrderDescriptionFragment())
        viewPager.adapter = ViewPagerAdapter(fragList, this)

        if(checkLocationPermission())
            requestLocationPermission()
    }
}