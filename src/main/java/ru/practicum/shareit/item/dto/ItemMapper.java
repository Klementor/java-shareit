package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemResponseDto toItemResponseDto(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();

        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.getAvailable());

        return itemResponseDto;
    }

    public static Item toItem(ItemRequestDto itemRequestDto) {
        Item item = new Item();

        item.setId(itemRequestDto.getId());
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());

        return item;
    }

    public static List<ItemResponseDto> fromItemListToItemResponseDtoList(Collection<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    public static ItemWithBookingsResponseDto toItemWithBookingsResponseDto(Item item,
                                                                            Booking lastBooking,
                                                                            Booking nextBooking,
                                                                            List<Comment> comments) {
        ItemWithBookingsResponseDto itemWithBookingsResponseDto = new ItemWithBookingsResponseDto();

        if (lastBooking != null) {
            itemWithBookingsResponseDto.setLastBooking(ItemWithBookingsResponseDto.BookingDto.fromBooking(lastBooking));
        }
        if (nextBooking != null) {
            itemWithBookingsResponseDto.setNextBooking(ItemWithBookingsResponseDto.BookingDto.fromBooking(nextBooking));
        }


        itemWithBookingsResponseDto.setId(item.getId());
        itemWithBookingsResponseDto.setName(item.getName());
        itemWithBookingsResponseDto.setDescription(item.getDescription());
        itemWithBookingsResponseDto.setAvailable(item.getAvailable());
        itemWithBookingsResponseDto.setComments(ItemWithBookingsResponseDto.CommentDto.toListCommentDto(comments));
        return itemWithBookingsResponseDto;
    }

    public static Comment toComment(CommentRequestDto commentRequestDto, User user, Item item, LocalDateTime time) {
        Comment comment = new Comment();

        comment.setText(commentRequestDto.getText());
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setTime(time);

        return comment;
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setText(comment.getText());
        commentResponseDto.setAuthorName(comment.getAuthor().getName());
        commentResponseDto.setCreated(comment.getTime());
        commentResponseDto.setId(comment.getId());

        return commentResponseDto;
    }
}
