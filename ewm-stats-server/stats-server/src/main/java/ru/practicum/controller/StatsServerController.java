package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.Constants.FORMATTER;
import static ru.practicum.Constants.HIT_ENDPOINT;
import static ru.practicum.Constants.STATS_ENDPOINT;
import static ru.practicum.Constants.YYYY_MM_DD_HH_MM_SS;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsServerController {
    private final StatsService statsService;

    @PostMapping(HIT_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHitDto dto) {
        log.debug("Request received POST '{}' : {}", HIT_ENDPOINT, dto);
        statsService.saveHit(dto);
    }

    @GetMapping(STATS_ENDPOINT)
    public List<ViewStatsDto> getStats(@RequestParam(name = "start")
                                       @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
                                       LocalDateTime start,
                                       @RequestParam(name = "end")
                                       @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
                                       LocalDateTime end,
                                       @RequestParam(name = "uris") String[] uris,
                                       @RequestParam(name = "unique", defaultValue = "false") boolean unique
    ) {
        final String pathStr = getPathStr(start, end, uris, unique);
        log.debug("Request received GET '{}?{}'", STATS_ENDPOINT, pathStr);

        return statsService.getStats(start, end, uris, unique)
                .stream().sorted()
                .collect(Collectors.toList());
    }

    private String getPathStr(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        final List<String> path = new ArrayList<>();
        if (start != null) path.add("start=" + start.format(FORMATTER));
        if (end != null) path.add("end=" + end.format(FORMATTER));
        if (uris != null) path.add("uris=" + String.join("&uris=", uris));
        path.add("unique=" + unique);

        return String.join("&", path);
    }
}