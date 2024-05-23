package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.TransactionPayment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class TransactionPaymentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer userId;
	private String orderId;
	private String transactionNo;
	private String methodPayment;
	private String typePayment;
	private String info;
	private Integer amount;
	private String bankCode;
	private String bankTranNo;
	private String cardType;
	private String ipAddress;
	private Long payDate;
	private String status;
	private String unit;
	private String secureHash;
	private String packagePremium;
	
	public TransactionPaymentDTO(TransactionPayment transactionPayment) {
		this.id = transactionPayment.getId();
		this.userId = transactionPayment.getUser().getId();
		this.orderId = transactionPayment.getOrderId();
		this.transactionNo = transactionPayment.getTransactionNo();
		this.methodPayment = transactionPayment.getMethodPayment();
		this.typePayment = transactionPayment.getTypePayment();
		this.info = transactionPayment.getInfo();
		this.amount = transactionPayment.getAmount();
		this.bankCode = transactionPayment.getBankCode();
		this.cardType = transactionPayment.getCardType();
		this.ipAddress = transactionPayment.getIpAddress();
		if (transactionPayment.getPayDate() != null) {
			this.payDate = transactionPayment.getPayDate().getTime();
		}
		this.status = transactionPayment.getStatus();
		this.unit = transactionPayment.getUnit();
		this.secureHash = transactionPayment.getSecureHash();
		this.packagePremium = transactionPayment.getPackagePremium();
	}
}
