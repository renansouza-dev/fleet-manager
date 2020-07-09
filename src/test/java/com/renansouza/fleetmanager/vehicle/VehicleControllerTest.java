package com.renansouza.fleetmanager.vehicle;

import com.renansouza.fleetmanager.AbstractMvcTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VehicleControllerTest extends AbstractMvcTest {

    @Autowired
    private MockMvc mvc;
    Vehicle vehicle;

    private static final String DOMAIN = "vehicles";

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setName("Ford KA");
        vehicle.setModel("KA");
        vehicle.setBrand("Ford");
        vehicle.setActive(true);
        vehicle.setFabricationYear(2020);
        vehicle.setCityConsumption(13.3);
        vehicle.setRoadConsumption(15.6);
    }

    @AfterEach
    void tearDown() {
        mvc = null;
        vehicle = null;
    }

    @Test
    @DisplayName("Check if context loads")
    void contextLoads() {
        assertThat(mvc).isNotNull();
    }

    @Test
    @DisplayName("Fail to find a vehicle with unknown id")
    public void canRetrieveByIdWhenDoesNotExist() throws Exception {
        final int notExpectedId = 2;
        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + notExpectedId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Could not find vehicle " + notExpectedId)));
    }

    @Test
    @DisplayName("Successfully find no vehicles")
    void all() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN)));
    }

    @Test
    @DisplayName("Successfully find one vehicle after adding")
    void one() throws Exception {
        final int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(vehicle)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/" + expectedId)));

        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + expectedId))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$.name", is(vehicle.getName())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/" + expectedId)));
    }

    @Test
    @DisplayName("Successfully update one vehicle after adding")
    void update() throws Exception {
        final int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(vehicle)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/" + expectedId)));

        vehicle.setName("Ford KA+");
        vehicle.setBrand("Fordy");
        vehicle.setModel("KA+");
        vehicle.setFabricationYear(2021);
        vehicle.setCityConsumption(14);
        vehicle.setRoadConsumption(17);

        mvc.perform(MockMvcRequestBuilders.put("/" + DOMAIN + "/" + expectedId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(vehicle)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(vehicle.getName())))
                .andExpect(jsonPath("$.brand", is(vehicle.getBrand())))
                .andExpect(jsonPath("$.model", is(vehicle.getModel())))
                .andExpect(jsonPath("$.fabricationYear", is(vehicle.getFabricationYear())))
                .andExpect(jsonPath("$.cityConsumption", is(vehicle.getCityConsumption())))
                .andExpect(jsonPath("$.roadConsumption", is(vehicle.getRoadConsumption())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/" + expectedId)));
    }

    @Test
    @DisplayName("Inactivate a vehicle")
    void delete() throws Exception {
        final int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(vehicle)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/" + expectedId)));

        mvc.perform(MockMvcRequestBuilders.delete("/" + DOMAIN + "/" + expectedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + expectedId))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$.active", is(false)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/" + expectedId)));

    }
}