package com.musicservice.catalog.mapper;

import com.musicservice.catalog.dto.post.ImageInfoPostDto;
import com.musicservice.domain.model.ImageInfo;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring")
@Primary
public interface ImageInfoMapperService {
    ImageInfo imageInfoDtoToImageInfo(ImageInfoPostDto source);
    ImageInfoPostDto imageInfoToImageInfoDto(ImageInfo destination);
}
