package com.musicservice.service.mapper;

import com.musicservice.dto.post.ImageInfoPostDto;
import com.musicservice.model.ImageInfo;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring")
@Primary
public interface ImageInfoMapper {
    ImageInfo imageInfoDtoToImageInfo(ImageInfoPostDto source);
    ImageInfoPostDto imageInfoToImageInfoDto(ImageInfo destination);
}
