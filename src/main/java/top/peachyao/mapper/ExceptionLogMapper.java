package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.ExceptionLog;

import java.util.List;

/**
 * @Description: 异常日志持久层接口
 * @Author: PeachYao
 * @Date: 2026-04-07
 */
@Mapper
public interface ExceptionLogMapper {
    List<ExceptionLog> getExceptionLogListByDate(String startDate, String endDate);
    int saveExceptionLog(ExceptionLog log);
    int deleteExceptionLogById(Long id);
}
