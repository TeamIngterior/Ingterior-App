package com.ing.ingterior.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.ing.ingterior.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class QuickAvatarImageView : AppCompatImageView, View.OnClickListener {

    private var mDrawable: Drawable? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        setOnClickListener(this)
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (mDrawable != null) mDrawable?.draw(canvas)
    }


    private fun setAvatarImageDrawable(drawable: Drawable) {
        super.setImageDrawable(drawable)
        mDrawable = drawable
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        mDrawable = null
    }

    fun setGroupAvatarImageView(user: User, size: Int, iconSize: Int){
        if(user.avatarURL.isEmpty()) {
            val drawable:Drawable = ContactAvatarDrawable(context, true, size, iconSize)
            setAvatarImageDrawable(drawable)
        }
        else{
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = withContext(Dispatchers.IO) {
                    val url = URL(user.avatarURL)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val inputStream: InputStream = connection.inputStream
                    BitmapFactory.decodeStream(inputStream)
                }
                setImageBitmap(bitmap)
            }
        }
    }


    override fun onClick(v: View?) {


    }

}