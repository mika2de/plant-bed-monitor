package de.mika.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mika.resource.model.MultiChart;
import de.mika.resource.model.SingleChart;
import de.mika.service.LocalDateTimeService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MoistureApiTest {

    @Inject
    LocalDateTimeService localDateTimeService;

    @BeforeAll
    public static void setup() {
        LocalDateTimeService mock = Mockito.mock(LocalDateTimeService.class);
        Mockito.when(mock.now()).thenReturn(LocalDateTime.of(2021,07,27,12,0,0,0));
        QuarkusMock.installMockForType(mock, LocalDateTimeService.class);
    }

    @Test
    public void get24hMoisture() throws JsonProcessingException {
        int hour = localDateTimeService.now().getHour();
        List<SingleChart> tomatoHourlyValues = new ArrayList<>();
        tomatoHourlyValues.add(new SingleChart(String.valueOf(hour-2), 10));
        tomatoHourlyValues.add(new SingleChart(String.valueOf(hour-1), 12));
        List<SingleChart> potatoHourlyValue = new ArrayList<>();
        potatoHourlyValue.add(new SingleChart(String.valueOf(hour-2), 20));
        potatoHourlyValue.add(new SingleChart(String.valueOf(hour-1), 21));

        MultiChart tomatoChart = new MultiChart("Tomato", tomatoHourlyValues);
        MultiChart potatoChart = new MultiChart("Potato", potatoHourlyValue);

        List<MultiChart> expectedMultiChartList = new ArrayList<>();
        expectedMultiChartList.add(tomatoChart);
        expectedMultiChartList.add(potatoChart);

        String actual = given()
                .when().get("/moisture/24h")
                .then()
                .statusCode(200)
                .extract().body().asString();
        List<MultiChart> actualObj = new ObjectMapper().readValue(actual, new TypeReference<List<MultiChart>>(){});
        Assertions.assertEquals(actualObj, expectedMultiChartList);
    }
}
