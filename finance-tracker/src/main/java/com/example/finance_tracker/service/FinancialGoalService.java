package com.example.finance_tracker.service;

import com.example.finance_tracker.Repositorys.FinancialGoalRepository;
import com.example.finance_tracker.model.FinancialGoal;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FinancialGoalService {
    private boolean paused;

    private final FinancialGoalRepository goalRepository;
    private  final EmailService emailService;
    public FinancialGoalService(FinancialGoalRepository goalRepository, EmailService emailService) {
        this.goalRepository = goalRepository;
        this.emailService = emailService;
    }

    // 1️⃣ Create a new financial goal
    public FinancialGoal createGoal(FinancialGoal goal) {
        goal.setSavedAmount(BigDecimal.ZERO);
        goal.setCompleted(false);
        return goalRepository.save(goal);
    }

    // 2️⃣ Get all goals for a user
    public List<FinancialGoal> getUserGoals(String userId) {
        return goalRepository.findByUserId(userId);
    }

    // 3️⃣ Update savings progress
    public FinancialGoal updateGoalProgress(String goalId, BigDecimal amount) {
        Optional<FinancialGoal> goalOpt = goalRepository.findById(goalId);
        if (goalOpt.isPresent()) {
            FinancialGoal goal = goalOpt.get();
            goal.setSavedAmount(goal.getSavedAmount().add(amount));

            if (goal.getSavedAmount().compareTo(goal.getTargetAmount()) >= 0) {
                goal.setCompleted(true);
            }

            return goalRepository.save(goal);
        }
        return null;
    }

    // 4️⃣ Delete a goal
    public void deleteGoal(String goalId) {
        goalRepository.deleteById(goalId);
    }
    private void allocateToGoals(String userId, BigDecimal incomeAmount) {
        List<FinancialGoal> goals = goalRepository.findByUserId(userId);

        for (FinancialGoal goal : goals) {
            if (!goal.isCompleted()) {
                BigDecimal allocation = incomeAmount.multiply(new BigDecimal("0.10"));
                goal.setSavedAmount(goal.getSavedAmount().add(allocation));

                // Check if goal is close to completion (90% or more)
                BigDecimal progress = goal.getSavedAmount().divide(goal.getTargetAmount(), 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"));

                if (progress.compareTo(new BigDecimal("90")) >= 0 && !goal.isCompleted()) {
                    sendGoalProgressNotification(userId, goal);
                }

                if (goal.getSavedAmount().compareTo(goal.getTargetAmount()) >= 0) {
                    goal.setCompleted(true);
                }

                goalRepository.save(goal);
            }
        }
    }
    private void sendGoalProgressNotification(String userId, FinancialGoal goal) {
        String subject = "🚀 Your Savings Goal is Almost Complete!";
        String message = "You're at " + goal.getSavedAmount() + " out of " + goal.getTargetAmount() +
                " for " + goal.getGoalName() + "! Keep going! 🎯";

        emailService.sendEmail(userId, subject, message);
    }
    public FinancialGoal pauseGoal(String goalId) {
        Optional<FinancialGoal> goalOpt = goalRepository.findById(goalId);
        if (goalOpt.isPresent()) {
            FinancialGoal goal = goalOpt.get();
            goal.setPaused(true);
            return goalRepository.save(goal);
        }
        return null;
    }

    public FinancialGoal resumeGoal(String goalId) {
        Optional<FinancialGoal> goalOpt = goalRepository.findById(goalId);
        if (goalOpt.isPresent()) {
            FinancialGoal goal = goalOpt.get();
            goal.setPaused(false);
            return goalRepository.save(goal);
        }
        return null;
    }

}
