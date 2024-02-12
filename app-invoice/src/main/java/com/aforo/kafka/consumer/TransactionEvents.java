package com.aforo.kafka.consumer;

import com.aforo.dao.InvoiceDao;
import com.aforo.model.Invoice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;



@Service
public class TransactionEvents {

    @Autowired
    private InvoiceDao _dao;

    @Autowired
    private ObjectMapper objectMapper;

    private Logger log = LoggerFactory.getLogger(TransactionEvents.class);

    @Transactional
    public void processTransactionEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        Invoice event = objectMapper.readValue(consumerRecord.value(), Invoice.class);
        log.info("Actulizando Invoice ***" + event.getIdInvoice());

        Optional<Invoice> optionalInvoice = _dao.findById(event.getIdInvoice());
        if (optionalInvoice.isPresent()) {
            Invoice oldInvoice = optionalInvoice.get();
            oldInvoice.setAmount(oldInvoice.getAmount() - event.getAmount());
            oldInvoice.setState(oldInvoice.getAmount() == 0 ? 1 : 0);

            _dao.updateInvoice(oldInvoice.getIdInvoice(), oldInvoice.getAmount(), oldInvoice.getState());
            log.info("Se ha pagado la factura # " + event.getIdInvoice());

        }else {
            log.info("No se encontro la factura # " + event.getIdInvoice());
        }

    }
}
