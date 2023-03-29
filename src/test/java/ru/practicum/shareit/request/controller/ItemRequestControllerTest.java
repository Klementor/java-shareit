package ru.practicum.shareit.request.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

@ContextConfiguration(classes = {ItemRequestController.class})
@ExtendWith(SpringExtension.class)
class ItemRequestControllerTest {
    @Autowired
    private ItemRequestController itemRequestController;

    @MockBean
    private RequestService requestService;

    /**
     * Method under test: {@link ItemRequestController#getAllItemRequests(Integer, Integer, Long)}
     */
    @Test
    void testGetAllItemRequests() throws Exception {
        when(requestService.getAllItemRequests((Integer) any(), (Integer) any(), (Long) any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/requests/all");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ItemRequestController#getItemRequestById(Long, Long)}
     */
    @Test
    void testGetItemRequestById() throws Exception {
        RequestResponseDto requestResponseDto = new RequestResponseDto();
        requestResponseDto.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        requestResponseDto.setDescription("The characteristics of someone or something");
        requestResponseDto.setId(1L);
        requestResponseDto.setItems(new ArrayList<>());
        when(requestService.getItemRequestById((Long) any(), (Long) any())).thenReturn(requestResponseDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests/{requestId}", 1L)
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"description\":\"The characteristics of someone or something\",\"created\":[1,1,1,1,1],"
                                        + "\"items\":[]}"));
    }

    /**
     * Method under test: {@link ItemRequestController#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterId() throws Exception {
        when(requestService.getItemRequestByRequesterId((Long) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ItemRequestController#postNewItemRequest(RequestDto, Long)}
     */
    @Test
    void testPostNewItemRequest() throws Exception {
        when(requestService.getItemRequestByRequesterId((Long) any())).thenReturn(new ArrayList<>());

        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("The characteristics of someone or something");
        String content = (new ObjectMapper()).writeValueAsString(requestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

