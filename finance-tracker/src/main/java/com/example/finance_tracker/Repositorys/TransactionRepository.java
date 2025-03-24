package com.example.finance_tracker.Repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.finance_tracker.model.Transaction;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Page<Transaction> findByUserId(String userId, Pageable pageable);

    Page<Transaction> findByUserIdAndCategory(String userId, String category, Pageable pageable);

    Page<Transaction> findByUserIdAndType(String userId, String type, Pageable pageable);

    Page<Transaction> findByUserIdAndCategoryAndType(String userId, String category, String type, Pageable pageable);
    List<Transaction> findByUserId(String userId);
    Page<Transaction> findByUserIdAndDateBetween(String userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0, type: 'EXPENSE' } }",
            "{ $group: { " +
                    "_id: { month: { $month: '$date' }, year: { $year: '$date' }, category: '$category' }, " +
                    "totalAmount: { $sum: { $toDouble: '$amount' } } } }",
            "{ $sort: { '_id.year': -1, '_id.month': -1 } }"
    })
    List<Map<String, Object>> getMonthlyExpenseReport(String userId);
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0, type: 'EXPENSE' } }",
            "{ $group: { _id: { month: { $month: '$date' }, year: { $year: '$date' } }, totalAmount: { $sum: { $toDouble: '$amount' } } } }",
            "{ $sort: { '_id.year': 1, '_id.month': 1 } }"
    })
    List<Map<String, Object>> getExpenseTrends(String userId);
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",  // ✅ Match transactions for a specific user
            "{ $group: { " +
                    "_id: { month: { $month: '$date' }, year: { $year: '$date' } }, " +
                    "totalIncome: { $sum: { $cond: { if: { $eq: ['$type', 'INCOME'] }, then: { $toDouble: '$amount' }, else: 0 } } }, " +
                    "totalExpense: { $sum: { $cond: { if: { $eq: ['$type', 'EXPENSE'] }, then: { $toDouble: '$amount' }, else: 0 } } } } }",
            "{ $sort: { '_id.year': -1, '_id.month': -1 } }"
    })

    List<Map<String, Object>> getIncomeVsExpenseTrends(String userId);
    List<Transaction> findByUserIdAndIsRecurringTrue(String userId);
    List<Transaction> findByIsRecurringTrue();
    boolean existsByUserIdAndCategoryAndDate(String userId, String category, LocalDateTime date);
    List<Transaction> findByUserIdAndCategoryAndDateBetween(String userId, String category, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
