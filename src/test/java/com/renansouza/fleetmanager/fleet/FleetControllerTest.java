package com.renansouza.fleetmanager.fleet;

import com.renansouza.fleetmanager.AbstractMvcTest;
import com.renansouza.fleetmanager.MvcUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FleetControllerTest extends AbstractMvcTest {

    @Autowired
    private MockMvc mvc;
    private static final String DOMAIN = "fleet";

    @Test
    @DisplayName("Check if context loads")
    void contextLoads() {
        assertThat(mvc).isNotNull();
    }

    @Test
    @DisplayName("Process cost")
    void process() throws Exception {
        for (int i = 0; i < MvcUtils.getVehicles().size(); i++) {
            int expectedId = i +1;

            mvc.perform(MockMvcRequestBuilders.post("/vehicles")
                    .contentType(MediaType.APPLICATION_JSON).content(mapToJson(MvcUtils.getVehicles().get(i))))
                    .andExpect(content().contentType(MediaTypes.HAL_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(expectedId)))
                    .andExpect(jsonPath("$._links.self.href", is("http://localhost/vehicles/" + expectedId)));
        }

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN + "/" + 3.99 + "/" + 100 + "/" + 100))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.vehicleForecasts", hasSize(3)))
                .andExpect(jsonPath("$._embedded.vehicleForecasts[0].vehicle.id", is(2)))
                .andExpect(jsonPath("$._embedded.vehicleForecasts[1].vehicle.id", is(1)))
                .andExpect(jsonPath("$._embedded.vehicleForecasts[2].vehicle.id", is(3)));
    }

    @Test
    @DisplayName("Process cost wihtout vehicles")
    void cantProcessWithoutVehicles() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN + "/" + 3.99 + "/" + 100 + "/" + 100))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("No vehicles found to process.")));
    }

}