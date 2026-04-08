package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.VisitLog;
import top.peachyao.model.dto.VisitLogUuidTimeDto;

import java.util.List;

/**
 * @Description: 访问日志持久层接口
 * @Author: PeachYao
 * @Date: 2026-04-08
 */
@Mapper
public interface VisitLogMapper {
    List<VisitLog> getVisitLogListByUUIDAndDate(String uuid, String startDate, String endDate);
    List<VisitLogUuidTimeDto> getUUIDAndCreateTimeByYesterday();
    int saveVisitLog(VisitLog log);
    int deleteVisitLogById(Long id);
}
