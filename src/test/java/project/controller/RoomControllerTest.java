package project.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import project.exceptions.RoomNotFoundException;
import project.models.entities.Address;
import project.models.entities.Equipment;
import project.models.entities.Room;
import project.models.entities.RoomType;
import project.models.entities.User;
import project.services.RoomService;

class RoomControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoomService roomService;

    private static final Room room = new Room();

    private static final String URL = "/api/rooms";

    @BeforeEach
    public void setUpBeforeEach() throws Exception {
        Address address = new Address();
        address.setCity("nantes");
        room.setAddress(address);
        room.setId(1);
        room.setOwner(user);
    }

    @Test
    public void testCreateWhenAuthenticated() throws Exception {
    	mockAuthentication();
        when(roomService.create(Mockito.any(), Mockito.any())).thenReturn(room);
        ResultActions result = mvc.perform(post(URL).headers(getAuthorizationHeaders()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(room)))
                .andExpect(status().isCreated());
        assertOnRoom(result);
    }

    @Test
    public void testCreateWhenUnauthenticated() throws Exception {
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(room)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateWithBadOwner() throws Exception {
        User user2 = new User();
        user2.setId(2);
        room.setOwner(user2);
        mvc.perform(post(URL).headers(getAuthorizationHeaders()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(room)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testFindAll() throws Exception {
        when(roomService.findAll(null, null, null, null, null)).thenReturn(List.of(room));

        ResultActions result = mvc.perform(get(URL).headers(getAuthorizationHeaders()))
                .andExpect(status().isOk());
        assertOnList(result);
    }

    @Test
    public void testFindAllWithNoResult() throws Exception {
        when(roomService.findAll(null, null, null, null, null)).thenThrow(RoomNotFoundException.class);
        mvc.perform(get(URL).headers(getAuthorizationHeaders()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindAllByUser() throws Exception {
        when(roomService.findByUserId(user.getId())).thenReturn(List.of(room));
        ResultActions result = mvc.perform(get(URL + "/users/" + user.getId()))
                .andExpect(status().isOk());
        assertOnList(result);

    }

    @Test
    public void testFindAllByUserNoResult() throws Exception {
        when(roomService.findByUserId(user.getId())).thenThrow(RoomNotFoundException.class);
        mvc.perform(get(URL + "/users/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindById() throws Exception {
        when(roomService.findById(room.getId())).thenReturn(room);
        ResultActions result = mvc.perform(get(URL + "/" + room.getId()))
                .andExpect(status().isOk());
        assertOnRoom(result);

    }

    @Test
    public void testFindByIdNoResult() throws Exception {
        when(roomService.findById(room.getId())).thenThrow(RoomNotFoundException.class);
        mvc.perform(get(URL + "/" + room.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindAllTypes() throws Exception {
        when(roomService.allTypes()).thenReturn(List.of(new RoomType("type")));
        mvc.perform(get(URL + "/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("type"));
    }

    @Test
    public void testFindAllEquipments() throws Exception {
        when(roomService.allEquipments()).thenReturn(List.of(new Equipment("equipment")));
        mvc.perform(get(URL + "/equipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("equipment"));
    }

    @Test
    public void testFindByType() throws Exception {
        when(roomService.findByType("type")).thenReturn(List.of(room));
        ResultActions result = mvc.perform(get(URL + "/types/type" ))
                .andExpect(status().isOk());
        assertOnList(result);
    }

    @Test
    public void testFindByTypeNoResult() throws Exception {
        when(roomService.findByType("type")).thenThrow(RoomNotFoundException.class);
        mvc.perform(get(URL + "/types/type"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByEquipment() throws Exception {
        when(roomService.findByEquipment("equipment")).thenReturn(List.of(room));
        ResultActions result = mvc.perform(get(URL + "/equipments/equipment"))
                .andExpect(status().isOk());
        assertOnList(result);
    }

    @Test
    public void testFindByEquipmentNoResult() throws Exception {
        when(roomService.findByEquipment("equipment")).thenThrow(RoomNotFoundException.class);
        mvc.perform(get(URL + "/equipments/equipment"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        mockAuthentication();
        doNothing().when(roomService).delete(room.getId(), user);
        mvc.perform(delete(URL + "/" + room.getId()).headers(getAuthorizationHeaders()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNotExist() throws Exception {
        mockAuthentication();
        doThrow(RoomNotFoundException.class).when(roomService).delete(room.getId(), user);
        mvc.perform(delete(URL + "/" + room.getId()).headers(getAuthorizationHeaders()))
                .andExpect(status().isNotFound());

    }

    private void assertOnList(ResultActions result) throws Exception {
        result
                .andExpect(jsonPath("$.[0].id").value(room.getId()))
                .andExpect(jsonPath("$.[0].address.city").value("nantes"))
                .andExpect(jsonPath("$.[0].owner.id").value(user.getId()));

    }

    private void assertOnRoom(ResultActions result) throws Exception {
        result
                .andExpect(jsonPath("id").value(room.getId()))
                .andExpect(jsonPath("address.city").value("nantes"))
                .andExpect(jsonPath("owner.id").value(user.getId()));
    }




}
