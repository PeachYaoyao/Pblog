package top.peachyao.model.dto;

import lombok.*;

import java.util.Date;

/**
 * @Description: 访客更新DTO
 * @Author: PeachYao
 * @Date: 2026-04-08
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitLogUuidTimeDto {
	private String uuid;
	private Date time;
	private Integer pv;
}
