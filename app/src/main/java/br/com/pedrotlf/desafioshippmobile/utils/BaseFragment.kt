package br.com.pedrotlf.desafioshippmobile.utils

import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {
//    lateinit var placesViewModel: PlacesViewModel
//    lateinit var orderViewModel: OrderViewModel
//
//    var layoutId: Int = 0
//    private var rootView: View? = null
//
//    val TAG: String
//        get() = this.javaClass.simpleName
//
//    /**
//     * ATENÇÃO! Por padrão, uma classe BaseFragment sempre fará um "RELOAD". Para desativar, basta
//     * inicializar seu fragment já setando essa variável para FALSE.
//     * */
//    var fragmentNeedsReloading: Boolean = true
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        placesViewModel = ViewModelProvider.AndroidViewModelFactory(act.application).create(
//            PlacesViewModel::class.java)
//        orderViewModel = ViewModelProvider.AndroidViewModelFactory(act.application).create(OrderViewModel::class.java)
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        if (rootView == null || fragmentNeedsReloading) {
//            rootView = inflater.inflate(layoutId, container, false)
//        } else {
//            val parent = rootView?.parent
//            if (parent != null) {
//                parent as ViewGroup
//                parent.removeView(rootView)
//            }
//        }
//        return rootView
//    }
//
//    fun setView(layoutId: Int){
//        this.layoutId = layoutId
//    }
}