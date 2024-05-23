package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.TransactionPaymentDAO;
import com.focusedapp.smartstudyhub.model.TransactionPayment;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.TransactionPaymentDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPaymentStatus;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Service
public class TransactionPaymentService {

	@Autowired TransactionPaymentDAO transactionPaymentDAO;
	
	/**
	 * Persist model in database
	 * 
	 * @param transactionPayment
	 * @return
	 */
	public TransactionPayment persistent(TransactionPayment transactionPayment) {
		return transactionPaymentDAO.save(transactionPayment);
	}
	
	public TransactionPayment findByOrderId(String orderId) {
		return transactionPaymentDAO.findByOrderId(orderId);
	}
	
	/**
	 * Get all Transaction payment info
	 * 
	 * @param user
	 * @return
	 */
	public List<TransactionPaymentDTO> getTransactionPayments(User user) {
		List<TransactionPayment> transactions = transactionPaymentDAO.findByUserIdAndStatus(user.getId(), 
				EnumPaymentStatus.SUCCESS.getValue());
		if (CollectionUtils.isEmpty(transactions)) {
			return new ArrayList<>();
		}
		return transactions.stream()
				.map(t -> new TransactionPaymentDTO(t))
				.sorted(Comparator.comparing(TransactionPaymentDTO::getPayDate).reversed())
				.collect(Collectors.toList());
	}
	
	public TransactionPaymentDTO getById(Integer id) {
		Optional<TransactionPayment> transactionPayment = transactionPaymentDAO.findById(id);
		if (transactionPayment.isEmpty()) {
			return null;
		}
		return new TransactionPaymentDTO(transactionPayment.get());
	}
	
}
