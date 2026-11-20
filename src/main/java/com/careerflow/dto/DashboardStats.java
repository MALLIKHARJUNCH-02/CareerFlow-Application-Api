package com.careerflow.dto;

/** Counts shown on the dashboard cards. */
public record DashboardStats(
        long total, long applied, long shortlisted, long interviews,
        long rejected, long selected, long offerReceived) {
}
