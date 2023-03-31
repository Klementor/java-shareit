package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {BookingController.class})
@ExtendWith(SpringExtension.class)
class BookingControllerTest {
    @Autowired
    private BookingController bookingController;

    @MockBean
    private BookingService bookingService;

    @Test
    void testAddBooking2() {
        BookingResponseDto.UserDto userDto = createUserDto();

        BookingResponseDto.ItemDto itemDto = createItemDto();

        BookingResponseDto bookingResponseDto = createBookingResponseDto(userDto, itemDto);

        BookingServiceImpl bookingServiceImpl = mock(BookingServiceImpl.class);
        when(bookingServiceImpl.addBooking((BookItemRequestDto) any(), (Long) any())).thenReturn(bookingResponseDto);
        BookingController bookingController = new BookingController(bookingServiceImpl);

        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        bookItemRequestDto.setItemId(1L);
        bookItemRequestDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        assertSame(bookingResponseDto, bookingController.addBooking(bookItemRequestDto, 1L));
        verify(bookingServiceImpl).addBooking((BookItemRequestDto) any(), (Long) any());
    }

    @Test
    void testUpdateBooking() throws Exception {
        BookingResponseDto.UserDto userDto = createUserDto();

        BookingResponseDto.ItemDto itemDto = createItemDto();

        BookingResponseDto bookingResponseDto = createBookingResponseDto(userDto, itemDto);

        when(bookingService.updateBooking((Long) any(), (Long) any(), (Boolean) any())).thenReturn(bookingResponseDto);
        MockHttpServletRequestBuilder patchResult = MockMvcRequestBuilders.patch("/bookings/{bookingId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = patchResult.param("approved", String.valueOf(true))
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"start\":[1,1,1,1,1],\"end\":[1,1,1,1,1],\"status\":\"WAITING\",\"booker\":{\"id\":1},\"item\":{\"id\":1,"
                                        + "\"name\":\"Name\"}}"));
    }

    @Test
    void testGetBookingOnlyForOwnerOrBooker() throws Exception {
        BookingResponseDto.UserDto userDto = createUserDto();

        BookingResponseDto.ItemDto itemDto = createItemDto();

        BookingResponseDto bookingResponseDto = createBookingResponseDto(userDto, itemDto);

        when(bookingService.getBookingOnlyForOwnerOrBooker((Long) any(), (Long) any())).thenReturn(bookingResponseDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/{bookingId}", 1L)
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"start\":[1,1,1,1,1],\"end\":[1,1,1,1,1],\"status\":\"WAITING\",\"booker\":{\"id\":1},\"item\":{\"id\":1,"
                                        + "\"name\":\"Name\"}}"));
    }

    @Test
    void testGetBookingsByUserId() throws Exception {
        when(bookingService.getBookingsByUserId((Long) any(), (String) any(), (Integer) any(), (Integer) any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/bookings");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetBookingsByOwnerId() throws Exception {
        when(bookingService.getBookingsByOwnerId((Integer) any(), (Integer) any(), (Long) any(), (String) any()))
                .thenReturn((Iterable<BookingResponseDto>) mock(Iterable.class));
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.get("/bookings/owner")
                .param("from", "https://example.org/example");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testGetBookingsByOwnerIdTwo() throws Exception {
        when(bookingService.getBookingsByOwnerId((Integer) any(), (Integer) any(), (Long) any(), (String) any()))
                .thenReturn((Iterable<BookingResponseDto>) mock(Iterable.class));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/bookings/owner");
        getResult.accept("https://example.org/example");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(406));
    }

    private BookingResponseDto.ItemDto createItemDto() {
        BookingResponseDto.ItemDto itemDto = new BookingResponseDto.ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Name");
        return itemDto;
    }

    private BookingResponseDto.UserDto createUserDto() {
        BookingResponseDto.UserDto userDto = new BookingResponseDto.UserDto();
        userDto.setId(1L);
        return userDto;
    }

    private BookingResponseDto createBookingResponseDto(BookingResponseDto.UserDto userDto, BookingResponseDto.ItemDto itemDto) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setBooker(userDto);
        bookingResponseDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingResponseDto.setId(1L);
        bookingResponseDto.setItem(itemDto);
        bookingResponseDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingResponseDto.setStatus(Booking.Status.WAITING);
        return bookingResponseDto;
    }
}

