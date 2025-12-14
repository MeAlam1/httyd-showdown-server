package com.mealam.showdown.utils.http.dto;

public record SuccessResponse<T>(T data) implements ApiResponse<T> {


}