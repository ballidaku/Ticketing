package example.com.ballidaku.commonClasses

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.util.*

object CommonSwitchFragmentsMethods {

    var TAG = javaClass.simpleName

    fun switchFragment(context: Context,
                       frameLayoutId: Int,
                       toWhere: Fragment,
                       willStoreInStack: Boolean,
                       json: String?) {

        val fragmentTransaction = (context as AppCompatActivity)
                .supportFragmentManager
                .beginTransaction()

        fragmentTransaction.replace(frameLayoutId, toWhere)

        if (willStoreInStack) {
            fragmentTransaction.addToBackStack(null)
        }

        if (json != null) {
            val bundle = Bundle()
            bundle.putString("DATA", json)
            toWhere.arguments = bundle
        }

        fragmentTransaction.commitAllowingStateLoss()
    }

    fun addFragment(context: Context,
                       frameLayoutId: Int,
                       toWhere: Fragment,
                       willStoreInStack: Boolean,
                       json: String?) {

        val fragmentTransaction = (context as AppCompatActivity)
                .supportFragmentManager
                .beginTransaction()

        fragmentTransaction.add(frameLayoutId, toWhere)

        if (willStoreInStack) {
            fragmentTransaction.addToBackStack(null)
        }

        if (json != null) {
            val bundle = Bundle()
            bundle.putString("DATA", json)
            toWhere.arguments = bundle
        }

        fragmentTransaction.commitAllowingStateLoss()
    }

    fun switchFragmentWithTag(context: Context,
                              frameLayoutId: Int,
                              toWhere: Fragment,
                              willStoreInStack: Boolean,
                              tag: String?) {

        val fragmentTransaction = (context as AppCompatActivity)
                .supportFragmentManager
                .beginTransaction()

        fragmentTransaction.replace(frameLayoutId, toWhere, tag)

        if (willStoreInStack) {
            fragmentTransaction.addToBackStack(null)
        }

        fragmentTransaction.commitAllowingStateLoss()
    }

    fun removeFragmentByTag(context: Context, tag: String?) {

        val supportFragmentManager = (context as AppCompatActivity)
                .supportFragmentManager

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()

        val fragmentByTag1 = supportFragmentManager.findFragmentByTag(tag)

        if (fragmentByTag1 != null) {
            fragmentTransaction.remove(fragmentByTag1)

            fragmentTransaction.commitAllowingStateLoss()

            supportFragmentManager.popBackStack()
        }
    }

    fun removeFragment(context: Context, fragment: Fragment?) {

        val supportFragmentManager = (context as AppCompatActivity)
                .supportFragmentManager

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()

        if (fragment != null) {
            fragmentTransaction.remove(fragment)

            fragmentTransaction.commitAllowingStateLoss()

            supportFragmentManager.popBackStack()
        }
    }

    fun getVisibleFragment(context: Context, container: Int): Fragment? {
        return (context as AppCompatActivity).supportFragmentManager.findFragmentById(container)
    }

    fun onBack(context: Context) {
        (context as AppCompatActivity).supportFragmentManager.popBackStackImmediate()
    }


    var analysisFragmentTagArrayList = ArrayList<String>()

    /* This is for analysis fragments*/
    fun switchAnalysisFragment(context: Context,
                               frameLayoutId: Int,
                               toWhere: Fragment,
                               willStoreInStack: Boolean,
                               json: String?) {

        val supportFragmentManager = (context as AppCompatActivity)
                .supportFragmentManager

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()

        val fragmentByTag = supportFragmentManager.findFragmentByTag(toWhere.javaClass.name)

//        if (fragmentByTag == null)
        if (!analysisFragmentTagArrayList.contains(toWhere.javaClass.name)) {

            analysisFragmentTagArrayList.add(toWhere.javaClass.name)

            fragmentTransaction.add(frameLayoutId, toWhere, toWhere.javaClass.name)

            if (willStoreInStack) {
                fragmentTransaction.addToBackStack(null)
            }

            if (json != null) {
                val bundle = Bundle()
                bundle.putString("DATA", json)
                toWhere.arguments = bundle
            }

            fragmentTransaction.commitAllowingStateLoss()

        } else {

            for (i in analysisFragmentTagArrayList.indices) {

                val fragmentByTag1 = supportFragmentManager.findFragmentByTag(analysisFragmentTagArrayList[i])

                if (fragmentByTag1 != null) {
                    fragmentTransaction.hide(fragmentByTag1)
                }
            }

            fragmentTransaction.show(fragmentByTag!!)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    fun removeAnalysisFragment(context: Context) {

        val supportFragmentManager = (context as AppCompatActivity)
                .supportFragmentManager

        val fragmentTransaction = supportFragmentManager
                .beginTransaction()

        for (i in analysisFragmentTagArrayList.indices) {

            val fragmentByTag1 = supportFragmentManager.findFragmentByTag(analysisFragmentTagArrayList[i])
            if (fragmentByTag1 != null) {
                fragmentTransaction.remove(fragmentByTag1)
            }
        }
        analysisFragmentTagArrayList.clear()
        fragmentTransaction.commitAllowingStateLoss()
    }


    fun getFragmentCount(context: Context): Int {
        return (context as AppCompatActivity).supportFragmentManager.backStackEntryCount
    }


    fun getStackFragments(context: Context) {
        for (fragment in (context as AppCompatActivity).supportFragmentManager.fragments) {
            Log.e(TAG, "fragment :" + fragment.javaClass.name)
            //            if (fragment instanceof technologies.waltado.upgame_pro.fragments.MainFragment)
//            {
//                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//            }
        }
    }

    fun getFragmentFromStack(context: Context, fragment: Fragment): Fragment? {
        for (stackFragment in (context as AppCompatActivity).supportFragmentManager.fragments) {
            Log.e(TAG, "fragment :" + fragment.javaClass.name)
            if (stackFragment.javaClass.name == fragment.javaClass.name) {
                return stackFragment
            }
        }
        return null
    }


}