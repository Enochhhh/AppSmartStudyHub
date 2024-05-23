package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Entity
@Table(name = "transaction_payment")
public class TransactionPayment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "transaction_no")
	private String transactionNo;
	
	@Column(name = "method_payment")
	private String methodPayment;
	
	@Column(name = "type_payment")
	private String typePayment;
	
	@Column(name = "info", length = 300)
	private String info;
	
	private Integer amount;
	
	@Column(name = "bank_code")
	private String bankCode;
	
	@Column(name = "bank_tran_no")
	private String bankTranNo;
	
	@Column(name = "card_type")
	private String cardType;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "pay_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date payDate;
	
	private String status;
	
	private String unit;
	
	@Column(name = "secure_hash")
	private String secureHash;
	
	@Column(name = "package_premium")
	private String packagePremium;
	
}
