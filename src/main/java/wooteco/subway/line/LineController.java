package wooteco.subway.line;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import wooteco.subway.exception.IllegalInputException;
import wooteco.subway.exception.NoSuchLineException;
import wooteco.subway.section.Section;
import wooteco.subway.section.SectionService;
import wooteco.subway.station.Station;
import wooteco.subway.station.StationService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;
    private final StationService stationService;

    @Autowired
    public LineController(LineService lineService, SectionService sectionService,
        StationService stationService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Line createdLine = lineService.createLine(new Line(lineRequest));
        stationService.showStation(lineRequest.getUpStationId());
        stationService.showStation(lineRequest.getDownStationId());
        Section createdSection = sectionService.createSection(createdLine.getId(), lineRequest);

        LineResponse lineResponse = LineResponse.from(createdLine);
        return ResponseEntity.created(URI.create("/lines/" + createdSection.getLineId())).body(lineResponse);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponses = lineService.showLines().stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable long id) {
        LineResponse lineResponse = LineResponse.from(lineService.showLine(id));
        return ResponseEntity.ok().body(lineResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateLine(@RequestBody LineRequest lineRequest, @PathVariable long id) {
        Line line = new Line(lineRequest);
        try {
            lineService.updateLine(id, line);
        } catch (NoSuchLineException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteLine(@PathVariable long id) {
        try {
            lineService.deleteLine(id);
        } catch (NoSuchLineException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
