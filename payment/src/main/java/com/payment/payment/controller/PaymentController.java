package com.payment.payment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.common.dto.CreatePaymentDocumentRequest;
import com.clinic.common.dto.CreatePaymentObligationRequest;
import com.clinic.mapper.ModelMapper;
import com.payment.payment.dto.PaymentDocumentRecord;
import com.payment.payment.dto.PaymentObligationRecord;
import com.payment.payment.entity.PaymentDocument;
import com.payment.payment.entity.PaymentObligation;
import com.payment.payment.repository.PaymentObligationRepository;
import com.payment.payment.service.PaymentService;

@RestController
@RequestMapping("/api/payment-obligations")
public class PaymentController {

	private final PaymentService paymentService;

	@Autowired
	private PaymentObligationRepository paymentObligationRepository;

	@Autowired
	private ModelMapper modelMapper;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping
	public ResponseEntity<PaymentObligation> createObligation(@RequestBody CreatePaymentObligationRequest request) {
		PaymentObligation obligation = paymentService.createObligation(request);
		return ResponseEntity.ok(obligation);
	}

	@PostMapping("/document")
	public ResponseEntity<PaymentDocument> createPaymentDocument(@RequestBody CreatePaymentDocumentRequest request) {
		PaymentDocument doc = paymentService.addPaymentDocument(request);
		return ResponseEntity.ok(doc);
	}

	@Transactional
	@GetMapping("/by-patient/{patientId}")
	public ResponseEntity<List<PaymentObligationRecord>> getObligationsForPatient(@PathVariable Long patientId) {
		List<PaymentObligation> obligations = paymentObligationRepository.findByPatientIdWithDocuments(patientId);

		List<PaymentObligationRecord> dtos = obligations.stream().map(ob -> {
			PaymentObligationRecord dto = new PaymentObligationRecord();
			dto.setPatientId(ob.getPatientId());
			dto.setAmount(ob.getAmount());
			dto.setDescription(ob.getDescription());
			dto.setDueDate(ob.getDueDate());
			dto.setPaid(ob.isPaid());

			List<PaymentDocumentRecord> docs = ob.getDocuments() != null ? ob.getDocuments().stream().map(doc -> {
				PaymentDocumentRecord docDto = new PaymentDocumentRecord();
				docDto.setId(doc.getId());
				docDto.setDocumentType(doc.getDocumentType());
				docDto.setPaymentType(doc.getPaymentType());
				docDto.setPaidAmount(doc.getPaidAmount());
				docDto.setSuccessful(doc.isSuccessful());
				docDto.setCreatedAt(doc.getCreatedAt());
				return docDto;
			}).limit(50).collect(Collectors.toList()) : new ArrayList<>();
			dto.setDocuments(docs);

			return dto;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}

}
