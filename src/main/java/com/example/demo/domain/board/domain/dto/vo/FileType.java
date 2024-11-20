package com.example.demo.domain.board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FileType {
	IMAGE,
	ATTACH;

	@JsonCreator
	public static FileType fromString(String value) {
		for(FileType fileType : FileType.values()) {
			if(fileType.toString().equalsIgnoreCase(value)) {
				return fileType;
			}
		}
		return null;
	}
}
