package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@JdbcTest
class SectionDaoTest {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    @Autowired
    public SectionDaoTest(JdbcTemplate jdbcTemplate) {
        this.lineDao = new JdbcLineDao(jdbcTemplate);
        this.stationDao = new JdbcStationDao(jdbcTemplate);
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @DisplayName("구간을 저장한다.")
    @Test
    void 구간_저장() {
        Line line = generateLine("2호선", "bg-green-600");
        Station upStation = generateStation("선릉역");
        Station downStation = generateStation("잠실역");
        Integer distance = 10;

        Section createdSection = sectionDao.save(
                new Section(line.getId(), upStation.getId(), downStation.getId(), distance));

        assertAll(
                () -> assertThat(createdSection.getLineId()).isEqualTo(line.getId()),
                () -> assertThat(createdSection.getUpStationId()).isEqualTo(upStation.getId()),
                () -> assertThat(createdSection.getDownStationId()).isEqualTo(downStation.getId()),
                () -> assertThat(createdSection.getDistance()).isEqualTo(distance)
        );
    }

    @DisplayName("노선 별 구간을 조회한다.")
    @Test
    void 노선별_구간조회() {
        Line line = generateLine("2호선", "bg-green-600");
        Station upStation1 = generateStation("선릉역");
        Station downStation1 = generateStation("잠실역");
        Integer distance1 = 10;
        Station upStation2 = generateStation("신도림역");
        Station downStation2 = generateStation("신대방역");
        Integer distance2 = 7;
        sectionDao.save(new Section(line.getId(), upStation1.getId(), downStation1.getId(), distance1));
        sectionDao.save(new Section(line.getId(), upStation2.getId(), downStation2.getId(), distance2));

        List<Section> sections = sectionDao.findByLineId(line.getId());

        assertThat(sections.size()).isEqualTo(2);
    }

    private Line generateLine(String name, String color) {
        return lineDao.save(new Line(name, color));
    }

    private Station generateStation(String name) {
        return stationDao.save(new Station(name));
    }
}
