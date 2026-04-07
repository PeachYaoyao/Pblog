package top.peachyao.service;

import org.springframework.scheduling.annotation.Async;
import top.peachyao.entity.ExceptionLog;
import top.peachyao.entity.LoginLog;

import java.util.List;

public interface LoginLogService {
    List<LoginLog> getLoginLogListByDate(String startDate, String endDate);
    @Async
    void saveLoginLog(ExceptionLog log);
    void deleteLoginLogById(Long id);
}
