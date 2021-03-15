package br.com.pedrotlf.desafioshippmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.pedrotlf.desafioshippmobile.establishments.EstablishmentsViewModel
import org.jetbrains.anko.support.v4.act

open class BaseFragment: Fragment() {
    lateinit var establishmentsViewModel: EstablishmentsViewModel

    var layoutId: Int = 0
    private var rootView: View? = null

    val TAG: String
        get() = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        establishmentsViewModel = ViewModelProvider.AndroidViewModelFactory(act.application).create(EstablishmentsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(layoutId, container, false)
        } else {
            val parent = rootView?.parent
            if (parent != null) {
                parent as ViewGroup
                parent.removeView(rootView)
            }
        }
        return rootView
    }

    fun setView(layoutId: Int){
        this.layoutId = layoutId
    }
}