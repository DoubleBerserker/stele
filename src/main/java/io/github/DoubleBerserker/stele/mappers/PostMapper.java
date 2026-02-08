package io.github.DoubleBerserker.stele.mappers;

import io.github.DoubleBerserker.stele.dto.PostResponseDto;
import io.github.DoubleBerserker.stele.dto.PostSummaryDto;
import io.github.DoubleBerserker.stele.entities.Post;
import io.github.DoubleBerserker.stele.projections.PostSummaryProjection;
import io.github.DoubleBerserker.stele.services.MarkdownService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MarkdownService.class, CategoryMapper.class})
public abstract class PostMapper {

    @Mapping(target = "content", qualifiedByName = "toHtml")
    public abstract PostResponseDto postToPostResponseDto(Post post);

    @Mapping(target = "content", qualifiedByName = "toHtml")
    public abstract PostSummaryDto postToPostSummaryDto(Post post);

    @Mapping(target = "content", qualifiedByName = "toSummarizedPlaintext")
    public abstract PostSummaryDto postSummaryProjectionToPostSummaryDto(PostSummaryProjection post);

}
