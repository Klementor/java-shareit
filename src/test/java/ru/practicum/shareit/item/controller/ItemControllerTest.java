package ru.practicum.shareit.item.controller;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsResponseDto;
import ru.practicum.shareit.item.service.ItemService;

@ContextConfiguration(classes = {ItemController.class})
@ExtendWith(SpringExtension.class)
class ItemControllerTest {
    @Autowired
    private ItemController itemController;

    @MockBean
    private ItemService itemService;

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.updateItem((Long) any(), (ItemRequestDto) any(), (Long) any()))
                .thenReturn(new ItemResponseDto());

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setAvailable(true);
        itemRequestDto.setDescription("The characteristics of someone or something");
        itemRequestDto.setId(1L);
        itemRequestDto.setName("Name");
        itemRequestDto.setRequestId(1L);
        String content = (new ObjectMapper()).writeValueAsString(itemRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/items/{itemId}", 1L)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":null,\"name\":null,\"description\":null,\"available\":null,\"requestId\":null}"));
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.getUserItems((Long) any())).thenReturn(new ArrayList<>());

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setAvailable(true);
        itemRequestDto.setDescription("The characteristics of someone or something");
        itemRequestDto.setId(1L);
        itemRequestDto.setName("Name");
        itemRequestDto.setRequestId(1L);
        String content = (new ObjectMapper()).writeValueAsString(itemRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetItemById() throws Exception {
        when(itemService.getItemById((Long) any(), (Long) any())).thenReturn(new ItemWithBookingsResponseDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items/{itemId}", 1L)
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"name\":null,\"description\":null,\"available\":null,\"lastBooking\":null,\"nextBooking\":null,"
                                        + "\"comments\":null}"));
    }

    @Test
    void testGetItemByIdTwo() throws Exception {
        when(itemService.getUserItems((Long) any())).thenReturn(new ArrayList<>());
        when(itemService.getItemById((Long) any(), (Long) any())).thenReturn(new ItemWithBookingsResponseDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items/{itemId}", "", "Uri Variables")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetUserItems() throws Exception {
        when(itemService.getUserItems((Long) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testSearchItemsByText() throws Exception {
        when(itemService.searchItemsByText((String) any(), (Long) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items/search")
                .param("text", "foo")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testPostComment() throws Exception {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setAuthorName("JaneDoe");
        commentResponseDto.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        commentResponseDto.setId(1L);
        commentResponseDto.setText("Text");
        when(itemService.postComment((Long) any(), (Long) any(), (CommentRequestDto) any()))
                .thenReturn(commentResponseDto);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        String content = (new ObjectMapper()).writeValueAsString(commentRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/items/{itemId}/comment", 1L)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"text\":\"Text\",\"authorName\":\"JaneDoe\",\"created\":[1,1,1,1,1]}"));
    }

    @Test
    void testPostCommentSecond() throws Exception {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setAuthorName("JaneDoe");
        commentResponseDto.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        commentResponseDto.setId(1L);
        commentResponseDto.setText("Text");
        when(itemService.postComment((Long) any(), (Long) any(), (CommentRequestDto) any()))
                .thenReturn(commentResponseDto);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("");
        String content = (new ObjectMapper()).writeValueAsString(commentRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/items/{itemId}/comment", 1L)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

