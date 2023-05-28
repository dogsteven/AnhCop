package com.dogsteven.anhcop.services.image

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.math.min

@Service
class ImageServiceImpl: ImageService {
    private val resourceDirectoryPath = Paths.get("resource").apply {
        if (!Files.exists(this)) {
            Files.createDirectories(this)
        }
    }

    override fun execute(command: ImageCommand.Load): ImageCommand.Load.Response {
        val filePath = resourceDirectoryPath.resolve(command.fileName)
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            val fileStream = Files.newInputStream(filePath)
            return ImageCommand.Load.Response(fileStream)
        } else {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Resource with name \"${command.fileName}\""
            )
        }
    }

    override fun execute(command: ImageCommand.Store): ImageCommand.Store.Response {
        val filePath = resourceDirectoryPath.resolve(command.fileName)
        if (!Files.exists(filePath)) {
            Files.createFile(filePath)
        }

        val image = BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB).apply {
            val cropped = ImageIO.read(command.fileStream).run {
                val size = min(width, height)
                val x = (width - size) / 2
                val y = (height - size) / 2
                getSubimage(x, y, size, size)
            }

            createGraphics().apply {
                drawImage(cropped, 0, 0, 300, 300, null)
                dispose()
            }
        }

        val outputStream = Files.newOutputStream(filePath)
        ImageIO.write(image, "png", outputStream)

        return ImageCommand.Store.Response
    }

    override fun execute(command: ImageCommand.Remove): ImageCommand.Remove.Response {
        val filePath = resourceDirectoryPath.resolve(command.fileName)

        if (Files.exists(filePath)) {
            Files.delete(filePath)
            return ImageCommand.Remove.Response
        } else {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Resource with name \"${command.fileName}\" does not exist"
            )
        }
    }
}