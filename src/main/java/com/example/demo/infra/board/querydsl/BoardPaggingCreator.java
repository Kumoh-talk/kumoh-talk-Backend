package com.example.demo.infra.board.querydsl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.demo.infra.board.entity.QBoard;
import com.querydsl.core.types.OrderSpecifier;

@Component
public class BoardPaggingCreator {

	public List<OrderSpecifier> createOrderSpecifiers(QBoard board, Pageable pageable) {
		System.out.println("pageable = " + pageable.getSort());
		return pageable.getSort().stream()
			.map(order -> {
			 	if (order.getProperty().equalsIgnoreCase("createdAt")) {
					return order.isAscending()
						? board.createdAt.asc()
						: board.createdAt.desc();
				} else if (order.getProperty().equalsIgnoreCase("updatedAt")) {
					return order.isAscending()
						? board.updatedAt.asc()
						: board.updatedAt.desc();
				}
				return null; // 해당하는 정렬 기준이 없으면 null
			})
			.filter(orderSpecifier -> orderSpecifier != null)
			.collect(Collectors.toList());
	}




}
