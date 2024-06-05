package com.focusedapp.smartstudyhub.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.TransactionPaymentDAO;
import com.focusedapp.smartstudyhub.model.TransactionPayment;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.StatisticalRevenueDTO;
import com.focusedapp.smartstudyhub.model.custom.TransactionPaymentDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPaymentStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumZoneId;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;


import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

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
	
	public TransactionPayment findById(Integer id) {
		Optional<TransactionPayment> transactionPayment = transactionPaymentDAO.findById(id);
		if (transactionPayment.isEmpty()) {
			return null;
		}
		return transactionPayment.get();
	}
	
	/**
	 * Statistical Revenue
	 * 
	 * @param typeQuery
	 * @return
	 */
	public List<StatisticalRevenueDTO> statisticalRevenue(String typeQuery, Integer year, Integer month) {
		Map<Long, Integer> mapRevenue = new LinkedHashMap<>();
		if (typeQuery.equals("ALL")) {
			LocalDateTime firstDateInYear = LocalDateTime.of(YearMonth.of(year, 1).atDay(1), LocalTime.MIDNIGHT); 
			LocalDateTime lastDateInYear = LocalDateTime.of(YearMonth.of(year, 12).atEndOfMonth(), LocalTime.MAX); 
			List<TransactionPayment> transactionPayments = transactionPaymentDAO
					.findByStatusAndPayDateGreaterThanEqualAndPayDateLessThan(EnumPaymentStatus.SUCCESS.getValue(), 
							Date.from(firstDateInYear.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()),
							Date.from(lastDateInYear.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()));
			if (!CollectionUtils.isEmpty(transactionPayments)) {
				mapRevenue = transactionPayments.stream()
						.collect(Collectors.groupingBy(t -> {
							LocalDateTime localDatePayDate = t.getPayDate().toInstant()
									.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()))
									.toLocalDateTime();
							LocalDateTime firstDateInMonth = localDatePayDate.with(firstDayOfMonth()).with(LocalTime.MIDNIGHT);
							return Date.from(firstDateInMonth.atZone(
									ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()).getTime();
						}, Collectors.summingInt(TransactionPayment::getAmount)));
			}
		} else {
			LocalDateTime firstDateInMonth = LocalDateTime.of(YearMonth.of(year, month).atDay(1), LocalTime.MIDNIGHT); 
			LocalDateTime lastDateInMonth = LocalDateTime.of(YearMonth.of(year, month).atEndOfMonth(), LocalTime.MAX); 
			List<TransactionPayment> transactionPayments = transactionPaymentDAO
					.findByStatusAndPayDateGreaterThanEqualAndPayDateLessThan(EnumPaymentStatus.SUCCESS.getValue(), 
							Date.from(firstDateInMonth.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()),
							Date.from(lastDateInMonth.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()));
			if (!CollectionUtils.isEmpty(transactionPayments)) {
				mapRevenue = transactionPayments.stream()
						.collect(Collectors.groupingBy(t -> {
							LocalDateTime localDatePayDate = t.getPayDate().toInstant()
									.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()))
									.toLocalDateTime();
							Integer dayWillMinus = localDatePayDate.getDayOfMonth() % 3 - 1;
							dayWillMinus = dayWillMinus < 0 ? 2 : dayWillMinus;
							localDatePayDate = localDatePayDate.minusDays(dayWillMinus);
							localDatePayDate = localDatePayDate.with(LocalTime.MIDNIGHT);
							return Date.from(localDatePayDate.atZone(
									ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()).getTime();
						}, Collectors.summingInt(TransactionPayment::getAmount)));
			}
		}
		return mapRevenue.entrySet().stream()
				.map(r -> new StatisticalRevenueDTO(r.getKey(), r.getValue()))
				.sorted(Comparator.comparing(StatisticalRevenueDTO::getFirstDateOfMonthOrDateInMonth))
				.collect(Collectors.toList());
	}
	
}
