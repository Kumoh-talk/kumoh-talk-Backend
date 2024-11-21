package com.example.demo.global.converter;

import org.springframework.core.convert.converter.Converter;

import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

public class BoardTypeConverter implements Converter<String, BoardType> {

	@Override
	public BoardType convert(String source) {
		for (BoardType boardType : BoardType.values()) {
			if (boardType.toString().equals(source)) {
				return boardType;
			}
		}
		throw new ServiceException(ErrorCode.BOARD_TYPE_PARSE_ERROR);
	}
}
