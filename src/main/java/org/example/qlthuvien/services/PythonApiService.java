package org.example.qlthuvien.services;


import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.helper.MultipartInputStreamFileResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PythonApiService {

    @Value("${python.backend_url}")
    private String pythonUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> sendImageAddIndex(MultipartFile image, Long bookId) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(image.getInputStream(), image.getOriginalFilename()));
            body.add("book_id", bookId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl + "/add", requestEntity, Map.class);

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Integer> sendImageSearchSimilar(MultipartFile image) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(image.getInputStream(), image.getOriginalFilename()));

            System.out.println(image.getOriginalFilename());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            System.out.println(requestEntity);
            ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl + "/search", requestEntity, Map.class);
            Map<String, Object> resultMap = response.getBody();
            Map<String, Object> result = response.getBody();
            List<Integer> results = (List<Integer>) resultMap.get("answer");
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ResponseEntity<Map<String, String>> deleteIndex(Long bookId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                pythonUrl + "/delete/" + bookId,
                HttpMethod.DELETE,
                requestEntity,
                Map.class
        );

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

}
