package se.paradoxia.pxdemo.util

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.graphics.*
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import se.paradoxia.pxdemo.BR
import se.paradoxia.pxdemo.personalinfo.viewmodel.PersonalInfoViewModel.CardPersonalInfo.PersonalInfoValue


@BindingAdapter("circleImageUrl")
fun setCircleImageUrl(imageView: ImageView, url: String?) {
    if (url == null) {
        imageView.setImageDrawable(null)
    } else {
        Picasso.with(imageView.context).load(url).transform(CircleTransform()).into(imageView)
    }
}

@BindingAdapter("circleImageRes")
fun setCircleImageRes(imageView: ImageView, @IdRes imageId: Int?) {
    if (imageId == null) {
        imageView.setImageDrawable(null)
    } else {
        Picasso.with(imageView.context).load(imageId).transform(CircleTransform()).into(imageView)
    }
}

class CircleTransform : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle"
    }
}

@BindingAdapter("entries", "layout")
fun setPersonalInfoValueList(
    viewGroup: ViewGroup,
    entries: ObservableList<PersonalInfoValue>?,
    @IdRes layoutId: Int
) {
    viewGroup.removeAllViews()
    if (entries != null) {
        val inflater =
            viewGroup.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for (i in entries.indices) {
            val binding =
                DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, viewGroup, true)
            binding.setVariable(BR.personalInfoValue, entries[i])
        }
    }
}