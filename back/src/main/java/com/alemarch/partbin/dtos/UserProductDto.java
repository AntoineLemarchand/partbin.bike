package com.alemarch.partbin.dtos;

public record UserProductDto(
    long id,
    String name,
    String description,
    long categoryId,
    long ownerId
) {}