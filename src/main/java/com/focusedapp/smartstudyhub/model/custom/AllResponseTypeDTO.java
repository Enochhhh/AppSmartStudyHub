package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.User;
import com.paypal.api.payments.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class AllResponseTypeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Boolean booleanType;
	private Integer integerType;
	private String stringType;
	private Long longType;
	private Integer transactionId;
	private User user;
	private Payment payment;
	private List<FilesDTO> files;
	private List<Object> objects;
}
