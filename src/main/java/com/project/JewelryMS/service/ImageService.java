package com.project.JewelryMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

@Service
public class ImageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String imgToBase64String(final RenderedImage img, final String formatName)
    {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try
        {
            ImageIO.write(img, formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (final IOException ioe)
        {
            throw new UncheckedIOException(ioe);
        }
    }

    public String pngToBase64String(String imagePath) {
        try {
            File imgFile = new File(imagePath);
            RenderedImage img = ImageIO.read(imgFile);

            if (img == null) {
                throw new IOException("Failed to read image file: " + imagePath);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);

            byte[] imageBytes = outputStream.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error converting PNG to Base64: " + e.getMessage(), e);
        }
    }

    public void updateProductImage(Long productId, String imagePath) {
        try {
            String base64String = pngToBase64String(imagePath);

            String updateSQL = "UPDATE ProductSell SET image = ? WHERE PK_productID = ?";
            jdbcTemplate.update(updateSQL, base64String, productId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
