package com.example.Foundation.repositories;

import com.example.Foundation.modal.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query(value = "SELECT * FROM payment_tbl WHERE donor_donor_id = ?1", nativeQuery = true)
    List<Payment> findByDonorId(int donorId);

//    @Query(value = "SELECT p.payment_id, p.amount, p.timestamp, p.receipt_id, " +
//            "d.donor_id, d.first_name, d.last_name " +
//            "FROM payment_tbl p " +
//            "INNER JOIN donor_tbl d ON p.donor_donor_id = d.donor_id",
//            nativeQuery = true)
//    List<PaymentDTO> findAllPaymentsDto();


//    @Query(value = "SELECT p.payment_id, p.amount, p.timestamp, p.receipt_id, " +
//            "d.donor_id, d.first_name, d.last_name " +
//            "FROM payment_tbl p " +
//            "INNER JOIN donor_tbl d ON p.donor_donor_id = d.donor_id",
//            nativeQuery = true)
//    @SqlResultSetMapping(name = "PaymentDTOMapping")
//    List<PaymentDTO> findAllPaymentsDto();


//    @NamedNativeQuery(
//            name = "findAllPaymentsDto",
//            query = "SELECT p.payment_id, p.amount, p.timestamp, p.receipt_id, " +
//                    "d.donor_id, d.first_name, d.last_name " +
//                    "FROM payment_tbl p " +
//                    "INNER JOIN donor_tbl d ON p.donor_donor_id = d.donor_id",
//            resultSetMapping = "PaymentDTOMapping"
//    )
//    @Query(name = "findAllPaymentsDto", nativeQuery = true)
//    List<PaymentDTO> findAllPaymentsDto();



}
