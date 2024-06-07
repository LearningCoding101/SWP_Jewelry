package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.Guarantee.CreateGuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeResponse;
import com.project.JewelryMS.repository.GuaranteeRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuaranteeService {

    @Autowired
    GuaranteeRepository guaranteeRepository;
    @Autowired
    ProductSellRepository productSellRepository;

    public GuaranteeResponse createGuarantee(CreateGuaranteeRequest createGuaranteeRequest) {
        Optional<ProductSell> productSellOptional = productSellRepository.findById(createGuaranteeRequest.getFK_productID());
        if (productSellOptional.isPresent()) {
            Guarantee guarantee = new Guarantee();

            guarantee.setProductSell(productSellOptional.get());
            guarantee.setPolicyType(createGuaranteeRequest.getPolicyType());
            guarantee.setCoverage(createGuaranteeRequest.getCoverage());

            Guarantee newGuarantee = guaranteeRepository.save(guarantee);
            return getGuaranteeResponseById(newGuarantee.getPK_guaranteeID());
        } else {
            throw new RuntimeException("Product ID not exist");
        }
    }

    public GuaranteeResponse getGuaranteeResponseById(long id) {
        return guaranteeRepository.findByGuaranteeId(id).orElse(null);
    }

    public List<GuaranteeResponse> readAllGuaranteeResponses() {
        return guaranteeRepository.listAllGuarantees();
    }

    public void updateGuaranteeDetails(GuaranteeRequest guaranteeRequest) {
        Optional<Guarantee> guaranteeUpdate = guaranteeRepository.findById(guaranteeRequest.getPK_guaranteeID());
        if (guaranteeUpdate.isPresent()) {
            Guarantee guarantee = guaranteeUpdate.get();

            guarantee.setProductSell(productSellRepository.findById(guaranteeRequest.getFK_productID()).orElse(null));
            guarantee.setCoverage(guaranteeRequest.getCoverage());
            guarantee.setPolicyType(guaranteeRequest.getPolicyType());


            guaranteeRepository.save(guarantee);
        }
    }

    public void deleteGuaranteePolicyById(long id) {
        Optional<Guarantee> guarantee = guaranteeRepository.findById(id);
        guarantee.ifPresent(guaranteeStatus -> {
            guaranteeStatus.setStatus(false); // Set status to false
            guaranteeRepository.save(guaranteeStatus);
        });
    }

    public List<GuaranteeResponse> readAllActiveGuaranteePolicies() {
        return guaranteeRepository.findByStatus(true);
    }

    public List<GuaranteeResponse> readAllInactiveGuaranteePolicies() {
        return guaranteeRepository.findByStatus(false);
    }

    public List<GuaranteeResponse> readAllGuaranteesByPolicyType(String policyType) {
        return guaranteeRepository.listAllGuaranteesByPolicyType(policyType);
    }

    public List<GuaranteeResponse> readAllGuaranteesByCoverage(String coverage) {
        return guaranteeRepository.listAllGuaranteesByCoverage(coverage);
    }

    public GuaranteeResponse readGuaranteeByProductId(long productId) {
        return guaranteeRepository.findByProductId(productId).orElse(null);
    }
}
