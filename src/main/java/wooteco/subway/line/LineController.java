package wooteco.subway.line;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.dto.LineDto;
import wooteco.subway.line.dto.LineIdDto;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.NonIdLineDto;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineRequest lineRequest) {
        NonIdLineDto nonIdLineDto = new NonIdLineDto(lineRequest.getName(), lineRequest.getColor());
        LineDto createdLineDto = lineService.createLine(nonIdLineDto);
        LineResponse lineResponse = new LineResponse(createdLineDto.getId(),
            createdLineDto.getName(), createdLineDto.getColor());

        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
            .body(lineResponse);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineDto> lines = lineService.findAll();
        List<LineResponse> lineResponses = lines.stream()
            .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor()))
            .collect(Collectors.toList());

        return ResponseEntity.ok()
            .body(lineResponses);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> showLine(@PathVariable final Long id) {
        LineDto lineDto = lineService.findOne(new LineIdDto(id));
        LineResponse lineResponse = new LineResponse(
            lineDto.getId(),
            lineDto.getName(),
            lineDto.getColor()
        );

        return ResponseEntity.ok()
            .body(lineResponse);

    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> updateLine(@RequestBody final LineRequest lineRequest,
        @PathVariable final Long id) {

        LineDto lineDto = new LineDto(id, lineRequest.getName(), lineRequest.getColor());
        lineService.update(lineDto);

        return ResponseEntity.ok()
            .build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.delete(new LineDto(id));

        return ResponseEntity.noContent()
            .build();
    }
}