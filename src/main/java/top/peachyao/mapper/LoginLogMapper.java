package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.ExceptionLog;
import top.peachyao.entity.LoginLog;

import java.util.List;

/**
 * @Description: 登录日志持久层接口
 * @Author: PeachYao
 * @Date: 2026-04-07
 */
@Mapper
public interface LoginLogMapper {
    List<LoginLog> getLoginLogListByDate(String startDate, String endDate);
    int saveLoginLog(LoginLog log);
    int deleteLoginLogById(Long id);
}
