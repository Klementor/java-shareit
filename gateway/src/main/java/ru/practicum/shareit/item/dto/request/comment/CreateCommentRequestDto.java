package ru.practicum.shareit.item.dto.request.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validation.group.CreationGroup;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
public class CreateCommentRequestDto {
    @NotEmpty(groups = {CreationGroup.class})
    private String text;
}
