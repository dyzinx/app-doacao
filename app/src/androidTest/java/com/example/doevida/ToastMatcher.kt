package com.example.doevida

import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ToastMatcher : TypeSafeMatcher<Root>() {

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    override fun matchesSafely(root: Root): Boolean {
        val type = root.windowLayoutParams?.get()?.type
        if (type == WindowManager.LayoutParams.TYPE_TOAST ||
            type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ||
            type == WindowManager.LayoutParams.TYPE_APPLICATION) {

            val windowToken: IBinder? = root.decorView.windowToken
            val appToken: IBinder? = root.decorView.applicationWindowToken

            // Toasts não têm decorView compartilhado com outras janelas
            return windowToken === appToken
        }
        return false
    }
}
