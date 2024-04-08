package com.ing.ingterior.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R

class ContactAvatarDrawable(private val context: Context, private val isMe: Boolean, private val size: Int) : Drawable() {

    private val sDefaultColor = ContextCompat.getColor(context, R.color.white)
    private val sMeColor = ContextCompat.getColor(context, R.color.gray_03)

    private val sDefaultAvatar = ContextCompat.getDrawable(context, R.drawable.ic_user_avatar)
    private val sMeAvatar = ContextCompat.getDrawable(context, R.drawable.ic_user_avatar_white)

    private val sPaint = Paint()

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        if (!isVisible || bounds.isEmpty) {
            return
        }
        drawUserAvatar(canvas)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    private fun drawUserAvatar(canvas: Canvas) {
        // Draw background color.
        sPaint.color = getColor()
        sPaint.alpha = mPaint.alpha
        val bounds = bounds
        val rect = RectF(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat())
        val cornerRadius = 8f
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, sPaint)


        val bitmap: Bitmap = getAvatar() ?: return
        canvas.drawBitmap(bitmap, (bounds.centerX() - bitmap.width / 2).toFloat(), (bounds.centerY() - bitmap.height / 2).toFloat(), sPaint)
    }


    private fun getColor(): Int {
        return if (isMe) sMeColor else sDefaultColor
    }

    private fun getAvatar(): Bitmap? {
        val vector = if(isMe) sMeAvatar ?: return null else sDefaultAvatar ?: return null
        var avatarWidth = vector.intrinsicWidth
        var avatarHeight = vector.intrinsicHeight
        if(size == 2) {
            avatarWidth = (avatarWidth/1.5f).toInt()
            avatarHeight = (avatarHeight/1.5f).toInt()
        }
        else if(size == 3 || size == 4){
            avatarWidth = (avatarWidth/2.0869565f).toInt()
            avatarHeight = (avatarHeight/2.0869565f).toInt()
        }
        logD("jypark", "avatarWidth=$avatarWidth, avatarHeight=$avatarHeight, intrinsicWidth=${vector.intrinsicWidth}, intrinsicHeight=${vector.intrinsicHeight}, size=$size")
        return try {
            val bitmap: Bitmap = Bitmap.createBitmap(
                avatarWidth,
                avatarHeight,
                Bitmap.Config.ARGB_8888
            )
            val c = Canvas(bitmap)
            vector.setBounds(0, 0, c.width, c.height)
            vector.draw(c)
            bitmap
        } catch (e: OutOfMemoryError) {
            // Handle the error
            null
        }
    }
}