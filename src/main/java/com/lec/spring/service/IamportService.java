package com.lec.spring.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;

@Service
public class IamportService {

    @Value("${imp.client.code}")
    private String clientCode;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    public String getToken() throws Exception {
        HttpsURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        String token = null;

        try {
            URL url = new URL("https://api.iamport.kr/users/getToken");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JsonObject json = new JsonObject();
            json.addProperty("imp_key", apiKey);
            json.addProperty("imp_secret", secretKey);

            bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(json.toString());
            bw.flush();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            Gson gson = new Gson();
            String responseLine = br.readLine();

            if (responseLine != null) {
                Map<String, Object> responseMap = gson.fromJson(responseLine, Map.class);
                String response = responseMap.get("response").toString();
                Map<String, Object> responseData = gson.fromJson(response, Map.class);
                token = responseData.get("access_token").toString();
            }
        } finally {
            if (bw != null) bw.close();
            if (br != null) br.close();
            if (conn != null) conn.disconnect();
        }

        System.out.println("토큰 받음~~" + token);
        return token;
    }

    public boolean cancelPayment(String impUid, String merchantUid, int amount, String reason) throws IamportResponseException, IOException {

        try {
            String token = getToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CancelData cancelData = new CancelData(impUid, false); // impUid를 기반으로 취소합니다.
        cancelData.setChecksum(BigDecimal.valueOf(amount));
        cancelData.setReason(reason);

        IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

        return cancelResponse != null && cancelResponse.getResponse() != null;
    }

    private void authenticate() {
        IamportResponse<AccessToken> authResponse = null;
        try {
            authResponse = iamportClient.getAuth();
        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (authResponse == null || authResponse.getResponse() == null) {
            throw new IllegalStateException("Iamport 인증에 실패했습니다.");
        }
    }
}


