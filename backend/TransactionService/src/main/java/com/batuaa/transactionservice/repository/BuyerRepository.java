package com.batuaa.transactionservice.repository;

import com.batuaa.transactionservice.model.Buyer;
import com.batuaa.transactionservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, String> {

    boolean existsByEmailIdAndRole(String emailId, Role role);

}
