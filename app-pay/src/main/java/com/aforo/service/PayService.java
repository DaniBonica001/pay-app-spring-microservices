package com.aforo.service;

import com.aforo.dao.PayDao;
import com.aforo.model.Pay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class PayService {

    @Autowired
    private PayDao _dao;

    private final RestTemplate restTemplate;

    private static final String APP_INVOICE_BASE_URL = "http://app-invoice:8006";

    @Autowired
    public PayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Pay registerPay(Pay pay) {
        String debtAmount = getInvoiceDetails(pay.getIdInvoice());
        if (debtAmount.equals("NO DETAILS FOUND")) {
            throw new RuntimeException("No se encontró la factura");
        }else {
            if (pay.getAmount() > Double.parseDouble(debtAmount) ) {
                throw new RuntimeException("La cantidad del pago es mayor que l a cantidad de la factura.");
            }
        }

        return _dao.save(pay);
    }

    private String getInvoiceDetails(Integer invoiceId) {
        String url = APP_INVOICE_BASE_URL + "/" + invoiceId;

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return "NO DETAILS FOUND";
        }
    }


}
