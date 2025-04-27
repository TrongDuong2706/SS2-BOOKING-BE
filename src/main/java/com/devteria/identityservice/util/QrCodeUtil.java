package com.devteria.identityservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class QrCodeUtil {

    /**
     * Chuyển đối tượng thành chuỗi JSON có định dạng đẹp.
     * @param object Đối tượng cần chuyển đổi thành JSON.
     * @return Chuỗi JSON định dạng đẹp của đối tượng.
     */
    public static String prettyObject(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Đăng ký module để hỗ trợ Java 8 date/time (LocalDate, LocalDateTime)
            objectMapper.registerModule(new JavaTimeModule());
            // Cấu hình để không sử dụng timestamp cho các kiểu ngày tháng
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // Chuyển đối tượng thành chuỗi JSON với định dạng đẹp
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Tạo mã QR từ dữ liệu và trả về dưới dạng chuỗi base64.
     * @param data Dữ liệu cần mã hóa thành mã QR.
     * @param width Chiều rộng của mã QR.
     * @param height Chiều cao của mã QR.
     * @return Chuỗi base64 đại diện cho mã QR dưới dạng hình ảnh PNG.
     * @throws WriterException Nếu không thể mã hóa dữ liệu thành mã QR.
     * @throws IOException Nếu có lỗi trong quá trình ghi hình ảnh.
     */
    public static String generateQrCode(String data, int width, int height) throws WriterException, IOException {
        StringBuilder result = new StringBuilder();
        if(!data.isEmpty()){
            // Tạo một luồng byte để lưu trữ hình ảnh mã QR
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // Tạo đối tượng QRCodeWriter để mã hóa dữ liệu thành mã QR
            QRCodeWriter writer = new QRCodeWriter();
            // Tạo ma trận bit từ dữ liệu và kích thước mã QR
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
            // Chuyển ma trận bit thành hình ảnh BufferedImage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            // Ghi hình ảnh BufferedImage vào luồng byte dưới định dạng PNG
            ImageIO.write(bufferedImage, "png", os);
            // Chuyển luồng byte thành chuỗi base64 và thêm tiền tố cho hình ảnh PNG
            result.append("data:image/png;base64,");
            result.append(new String(Base64.getEncoder().encode(os.toByteArray())));
        }
        return result.toString();
    }
}
