package com.vitoria.portfolio_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class CloudinaryStorageService implements ImageStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");
    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024;
    private static final String UPLOAD_ENDPOINT = "https://api.cloudinary.com/v1_1/%s/image/upload";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${cloudinary.url:}")
    private String cloudinaryUrl;

    @Value("${cloudinary.upload-preset:}")
    private String uploadPreset;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        validate(file);
        CloudinaryCredentials credentials = credentials();

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String publicId = "uploads/" + UUID.randomUUID();

        String signature = null;
        if (credentials.apiSecret() != null && !credentials.apiSecret().isBlank()) {
            TreeMap<String, String> paramsToSign = new TreeMap<>();
            paramsToSign.put("public_id", publicId);
            paramsToSign.put("timestamp", timestamp);
            signature = sign(paramsToSign, credentials.apiSecret());
        } else if (uploadPreset == null || uploadPreset.isBlank()) {
            throw new IllegalStateException(
                    "Configure cloudinary.upload-preset (upload não assinado) ou forneça api_secret na CLOUDINARY_URL");
        }

        MultipartBody multipart = buildMultipartBody(file, credentials.apiKey(), timestamp,
                publicId, signature, uploadPreset);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(UPLOAD_ENDPOINT, credentials.cloudName())))
                .header("Content-Type", "multipart/form-data; boundary=" + multipart.boundary())
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipart.content()))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Upload interrompido", e);
        }
        if (response.statusCode() != 200) {
            throw new IOException("Cloudinary retornou HTTP " + response.statusCode() + ": " + response.body());
        }

        JsonNode json = objectMapper.readTree(response.body());
        return json.get("secure_url").asText();
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio");
        }
        if (file.getContentType() == null || !ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Use JPEG, PNG, WEBP ou GIF");
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException("Arquivo excede o limite de 5MB");
        }
    }

    private CloudinaryCredentials credentials() {
        if (cloudinaryUrl == null || cloudinaryUrl.isBlank()) {
            throw new IllegalStateException(
                    "cloudinary.url não configurado. Defina CLOUDINARY_URL como cloudinary://api_key:api_secret@cloud_name ou apenas o cloud name");
        }
        String rest = cloudinaryUrl.startsWith("cloudinary://")
                ? cloudinaryUrl.substring("cloudinary://".length())
                : cloudinaryUrl;

        String apiKey = null;
        String apiSecret = null;
        String cloudName = rest;

        if (rest.contains("@")) {
            String[] atSplit = rest.split("@", 2);
            String[] keySecret = atSplit[0].split(":", 2);
            if (keySecret.length == 2 && !keySecret[1].equals("api_secret")) {
                apiKey = keySecret[0];
                apiSecret = keySecret[1];
            } else {
                apiKey = keySecret[0];
            }
            cloudName = atSplit[1];
        }
        if (cloudName.isBlank()) {
            throw new IllegalStateException("CLOUDINARY_URL em formato inválido: cloud name ausente");
        }
        return new CloudinaryCredentials(apiKey, apiSecret, cloudName);
    }

    private String sign(TreeMap<String, String> params, String apiSecret) {
        String paramsString = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");
        return sha1Hex(paramsString + apiSecret);
    }

    private String sha1Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return HexFormat.of().formatHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao gerar assinatura", e);
        }
    }

    private MultipartBody buildMultipartBody(MultipartFile file, String apiKey, String timestamp,
                                            String publicId, String signature, String preset) throws IOException {
        String boundary = "----devinboundary" + UUID.randomUUID().toString().replace("-", "");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
        String contentType = file.getContentType();

        if (apiKey != null && !apiKey.isBlank()) {
            appendField(out, boundary, "api_key", apiKey);
        }
        appendField(out, boundary, "timestamp", timestamp);
        appendField(out, boundary, "public_id", publicId);
        if (signature != null) {
            appendField(out, boundary, "signature", signature);
        }
        if (preset != null && !preset.isBlank()) {
            appendField(out, boundary, "upload_preset", preset);
        }
        appendField(out, boundary, "file", filename, contentType, file.getBytes());

        out.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        return new MultipartBody(out.toByteArray(), boundary);
    }

    private record MultipartBody(byte[] content, String boundary) {
    }

    private void appendField(ByteArrayOutputStream out, String boundary, String name, String value) throws IOException {
        out.write(("--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n" +
                value + "\r\n").getBytes(StandardCharsets.UTF_8));
    }

    private void appendField(ByteArrayOutputStream out, String boundary, String name,
                             String filename, String contentType, byte[] content) throws IOException {
        out.write(("--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n" +
                "Content-Type: " + contentType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(content);
        out.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    private record CloudinaryCredentials(String apiKey, String apiSecret, String cloudName) {
    }
}
