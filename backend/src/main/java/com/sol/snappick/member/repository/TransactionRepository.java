package com.sol.snappick.member.repository;

import com.sol.snappick.member.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE FUNCTION('DATE_FORMAT', t.transactedAt, '%Y%m%d') = :dateString " +
            "ORDER BY t.transactedAt DESC")
    List<Transaction> findTransactionsByDateString(@Param("dateString") String dateString);
}
