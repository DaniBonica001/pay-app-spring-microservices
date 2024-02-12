package com.aforo.dao;

import com.aforo.model.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;


import java.util.List;

@Repository
public interface InvoiceDao extends CrudRepository<Invoice, Integer> {

    @Query("select i from Invoice i order by i.idInvoice")
    List<Invoice> findAllInvoices();

    @Modifying
    @Query("update Invoice i set i.amount = :amount , i.state = :state where i.idInvoice = :invoice")
    void updateInvoice(Integer invoice, Double amount, Integer state);
}
