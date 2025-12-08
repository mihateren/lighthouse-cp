package ru.mai.lighthouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lighthouse.entity.api.batch.BatchError;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryRequest;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryResponse;
import ru.mai.lighthouse.repository.search_diary.SearchDiaryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchDiaryService {

    private final SearchDiaryRepository searchDiaryRepository;

    public SearchDiaryResponse create(SearchDiaryRequest request) {
        log.info("Создание записи в дневнике для команды: {}", request.getTeamId());
        return searchDiaryRepository.create(request);
    }

    public SearchDiaryResponse getById(Long id) {
        log.info("Получение записи дневника по ID: {}", id);
        return searchDiaryRepository.getById(id)
                .orElseThrow(() -> new ru.mai.lighthouse.exception.NotFoundException(
                        String.format("Запись дневника с ID %d не найдена", id)));
    }

    public List<SearchDiaryResponse> getAll() {
        log.info("Получение всех записей дневника");
        return searchDiaryRepository.getAll();
    }

    public List<SearchDiaryResponse> getByTeam(Long teamId) {
        log.info("Получение записей дневника по команде: {}", teamId);
        return searchDiaryRepository.getByTeam(teamId);
    }

    public List<SearchDiaryResponse> getByVolunteer(Long volunteerId) {
        log.info("Получение записей дневника по волонтеру: {}", volunteerId);
        return searchDiaryRepository.getByVolunteer(volunteerId);
    }

    public SearchDiaryResponse update(Long id, SearchDiaryRequest request) {
        log.info("Обновление записи дневника: {}", id);
        return searchDiaryRepository.update(id, request);
    }

    public void delete(Long id) {
        log.info("Удаление записи дневника: {}", id);
        searchDiaryRepository.delete(id);
    }

    public BatchResponse<SearchDiaryResponse> batchCreate(BatchRequest<SearchDiaryRequest> request) {
        log.info("Пакетное создание записей дневника: {} элементов", request.getItems().size());

        List<SearchDiaryResponse> successItems = request.getItems().stream()
                .map(searchDiaryRepository::create)
                .toList();

        return new BatchResponse<SearchDiaryResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(0)
                .setSuccessItems(successItems)
                .setErrors(List.of());
    }

    public BatchResponse<SearchDiaryResponse> batchUpdate(BatchRequest<SearchDiaryRequest> request) {
        log.info("Пакетное обновление записей дневника: {} элементов", request.getItems().size());

        List<BatchError> errors = IntStream.range(0, request.getItems().size())
                .mapToObj(index -> new BatchError(index, "Для обновления требуется ID записи"))
                .toList();

        return new BatchResponse<SearchDiaryResponse>()
                .setSuccessCount(0)
                .setErrorCount(request.getItems().size())
                .setSuccessItems(List.of())
                .setErrors(errors);
    }
}
