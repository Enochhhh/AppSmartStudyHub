package com.focusedapp.smartstudyhub.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.TransactionPayment;

@Repository
public interface TransactionPaymentDAO extends JpaRepository<TransactionPayment, Integer> {

	TransactionPayment findByOrderId(String orderId);
	
	List<TransactionPayment> findByUserIdAndStatus(Integer userId, String status);
	
	List<TransactionPayment> findByStatusAndPayDateGreaterThanEqualAndPayDateLessThan(String status, Date startDate, Date endDate);
}
