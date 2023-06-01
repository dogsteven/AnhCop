package com.dogsteven.anhcop.utils

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.min

class BufferedImageUtils {
    companion object {
        fun BufferedImage.centerCrop(): BufferedImage {
            val size = min(width, height)

            return getSubimage(
                (width - size) / 2, (height - size) / 2, size, size
            )
        }

        fun BufferedImage.scaleTo(newWidth: Int, newHeight: Int): BufferedImage {
            return BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB).apply {
                createGraphics().apply {
                    drawImage(this@scaleTo, 0, 0, newWidth, newHeight, null)
                    dispose()
                }
            }
        }

        fun BufferedImage.toByteArray(format: String = "png"): ByteArray {
            return ByteArrayOutputStream().apply {
                ImageIO.write(this@toByteArray, format, this)
            }.toByteArray()
        }
    }
}