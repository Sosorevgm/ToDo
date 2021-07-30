package com.sosorevgm.todo.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class VectorDrawableMatcher(@DrawableRes private val drawableRes: Int) :
    TypeSafeMatcher<View?>(View::class.java) {
    private var resourceName: String? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) {
            return false
        }
        val imageView: ImageView = item
        if (drawableRes < 0) {
            return imageView.drawable == null
        }
        val resources: Resources = item.getContext().resources
        val expectedDrawable: Drawable = resources.getDrawable(
            drawableRes, null
        )
        resourceName = resources.getResourceEntryName(drawableRes)
        val draw: Drawable = imageView.drawable as VectorDrawable
        val drawTwo: Drawable = expectedDrawable as VectorDrawable
        return draw.constantState?.equals(drawTwo.constantState) ?: false
    }

    override fun describeTo(description: Description?) {
        description?.let {
            it.appendText("with drawable from resource id: ")
            it.appendValue(drawableRes)
            if (resourceName != null) {
                it.appendText("[")
                it.appendText(resourceName)
                it.appendText("]")
            }
        }
    }
}

fun withVectorDrawable(@DrawableRes drawableRes: Int): Matcher<View?> {
    return VectorDrawableMatcher(drawableRes)
}