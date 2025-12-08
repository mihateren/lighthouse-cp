package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Reports", description = "API для получения аналитических отчетов и статистики")
public class ReportController {

    public static final String BASE_PREFIX = API_V1 + "/reports";
    public static final String OPERATIONS_STATISTICS = BASE_PREFIX + "/operations/statistics";
    public static final String OPERATIONS_REPORT = BASE_PREFIX + "/operations/report";
    public static final String VOLUNTEER_ACTIVITY = BASE_PREFIX + "/volunteers/activity";
    public static final String TOP_VOLUNTEERS = BASE_PREFIX + "/volunteers/top";
    public static final String TEAMS_STATISTICS = BASE_PREFIX + "/teams/statistics";
    public static final String OPERATION_STATUS_HISTORY = BASE_PREFIX + "/operations/{operationId}/status-history";
    public static final String ACTIVE_OPERATIONS_BY_CENTER = BASE_PREFIX + "/centers/{centerId}/active-operations";
    public static final String AVG_VICTIM_AGE_BY_CITY = BASE_PREFIX + "/victims/avg-age/{city}";
    public static final String VOLUNTEER_RATING = BASE_PREFIX + "/volunteers/{volunteerId}/rating";

    @GetMapping(OPERATIONS_STATISTICS)
    @Operation(summary = "Статистика по операциям", 
               description = "Возвращает агрегированную статистику по операциям поиска (из VIEW v_operations_statistics)")
    public Result<List<Map<String, Object>>> getOperationsStatistics() {
        return Result.success(List.of());
    }

    @GetMapping(OPERATIONS_REPORT)
    @Operation(summary = "Отчет по операциям за период", 
               description = "Возвращает отчет по операциям за указанный период (использует функцию get_operations_report)")
    public Result<List<Map<String, Object>>> getOperationsReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String status) {
        return Result.success(List.of());
    }

    @GetMapping(VOLUNTEER_ACTIVITY)
    @Operation(summary = "Активность волонтеров", 
               description = "Возвращает информацию об активности волонтеров (из VIEW v_volunteer_activity)")
    public Result<List<Map<String, Object>>> getVolunteerActivity() {
        return Result.success(List.of());
    }

    @GetMapping(TOP_VOLUNTEERS)
    @Operation(summary = "Топ волонтеров", 
               description = "Возвращает топ волонтеров по рейтингу (использует функцию get_top_volunteers)")
    public Result<List<Map<String, Object>>> getTopVolunteers(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long centerId) {
        return Result.success(List.of());
    }

    @GetMapping(TEAMS_STATISTICS)
    @Operation(summary = "Статистика по командам", 
               description = "Возвращает статистику по командам волонтеров (из VIEW v_teams_statistics)")
    public Result<List<Map<String, Object>>> getTeamsStatistics() {
        return Result.success(List.of());
    }

    @GetMapping(OPERATION_STATUS_HISTORY)
    @Operation(summary = "История статусов операции", 
               description = "Возвращает историю изменений статусов для указанной операции (из VIEW v_operation_status_history)")
    public Result<List<Map<String, Object>>> getOperationStatusHistory(@PathVariable Long operationId) {
        return Result.success(List.of());
    }

    @GetMapping(ACTIVE_OPERATIONS_BY_CENTER)
    @Operation(summary = "Количество активных операций по центру", 
               description = "Возвращает количество активных операций для указанного центра (использует функцию count_active_operations_by_center)")
    public Result<Integer> getActiveOperationsByCenter(@PathVariable Long centerId) {
        return Result.success(0);
    }

    @GetMapping(AVG_VICTIM_AGE_BY_CITY)
    @Operation(summary = "Средний возраст пропавших по городу", 
               description = "Возвращает средний возраст пропавших людей в указанном городе (использует функцию avg_victim_age_by_city)")
    public Result<Double> getAvgVictimAgeByCity(@PathVariable String city) {
        return Result.success(0.0);
    }

    @GetMapping(VOLUNTEER_RATING)
    @Operation(summary = "Рейтинг волонтера", 
               description = "Возвращает рейтинг указанного волонтера (использует функцию calculate_volunteer_rating)")
    public Result<Double> getVolunteerRating(@PathVariable Long volunteerId) {
        return Result.success(0.0);
    }
}

