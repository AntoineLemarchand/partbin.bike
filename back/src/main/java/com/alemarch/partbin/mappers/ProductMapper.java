package com.alemarch.partbin.mappers;

import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.alemarch.partbin.dtos.CreateProductDto;
import com.alemarch.partbin.dtos.ProductDto;
import com.alemarch.partbin.entities.Product;

@Mapper(componentModel = "spring", uses = {OwnerMapper.class, CategoryMapper.class})
public interface ProductMapper {
	
	@Mapping(target = "imagesUrl", source = "imagePaths", qualifiedByName = "mapImagePaths")
	ProductDto toDto(Product product);

	Product toEntity(ProductDto productDto);

	Product toEntity(CreateProductDto productDto);
	
	@Named("mapImagePaths")
	default Set<String> mapImagePaths(List<String> imagePaths) {
		if (imagePaths == null || imagePaths.isEmpty()) {
			return null;
		}
		return Set.copyOf(imagePaths);
	}
}
