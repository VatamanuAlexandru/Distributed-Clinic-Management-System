package com.payment.payment.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinic.common.dto.CreatePaymentDocumentRequest;
import com.clinic.common.dto.CreatePaymentObligationRequest;
import com.payment.payment.entity.PaymentDocument;
import com.payment.payment.entity.PaymentObligation;
import com.payment.payment.repository.PaymentDocumentRepository;
import com.payment.payment.repository.PaymentObligationRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentObligationRepository paymentObligationRepository;

	@Autowired
	private PaymentDocumentRepository paymentDocumentRepository;

	@Transactional
	public PaymentObligation createObligation(CreatePaymentObligationRequest request) {
		PaymentObligation obligation = new PaymentObligation();
		obligation.setPatientId(request.getPatientId());
		obligation.setAmount(request.getAmount());
		obligation.setDescription(request.getDescription());
		obligation.setDueDate(request.getDueDate());
		obligation.setPaid(false);

		return paymentObligationRepository.save(obligation);
	}

	@Transactional
	public PaymentDocument addPaymentDocument(CreatePaymentDocumentRequest request) {
		PaymentObligation obligation = paymentObligationRepository.findById(request.getObligationId())
				.orElseThrow(() -> new IllegalArgumentException("Obligație de plată inexistentă"));

		PaymentDocument document = new PaymentDocument();
		document.setPaymentObligation(obligation);
		document.setDocumentType(request.getDocumentType());
		document.setPaymentType(request.getPaymentType());
		document.setPaidAmount(request.getPaidAmount());
		document.setSuccessful(request.isSuccessful());

		PaymentDocument savedDoc = paymentDocumentRepository.save(document);

		BigDecimal paidSum = paymentDocumentRepository.sumPaidForObligation(obligation.getId());

		if (paidSum != null && paidSum.compareTo(obligation.getAmount()) >= 0) {
			obligation.setPaid(true);
		} else {
			obligation.setPaid(false);
		}
		paymentObligationRepository.save(obligation);

		return savedDoc;
	}

}
