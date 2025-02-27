package com.example.demo.domain.base.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;

@Getter
public class GlobalPageableDto<T> {
	private Pageable pageable;
	private Page<T> page;

	public static GlobalPageableDto create(Pageable pageable) {
		GlobalPageableDto globalPageableDto = new GlobalPageableDto();
		globalPageableDto.pageable = pageable;
		return globalPageableDto;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}
}
