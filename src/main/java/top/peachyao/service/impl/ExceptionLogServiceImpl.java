package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.entity.ExceptionLog;
import top.peachyao.exception.PersistenceException;
import top.peachyao.mapper.ExceptionLogMapper;
import top.peachyao.model.dto.UserAgentDTO;
import top.peachyao.service.ExceptionLogService;
import top.peachyao.util.IpAddressUtils;
import top.peachyao.util.UserAgentUtils;

import java.util.List;

/**
 * @Description: 异常日志业务层实现
 * @Author: PeachYao
 * @Date: 2026-04-07
 */
@Service
public class ExceptionLogServiceImpl implements ExceptionLogService {
    @Autowired
    ExceptionLogMapper exceptionLogMapper;
    @Autowired
    UserAgentUtils userAgentUtils;
    @Override
    public List<ExceptionLog> getExceptionLogListByDate(String startDate, String endDate) {
        return exceptionLogMapper.getExceptionLogListByDate(startDate, endDate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExceptionLog(ExceptionLog log) {
        String ipSource = IpAddressUtils.getCityInfo(log.getIp());
        UserAgentDTO userAgentDTO = userAgentUtils.parseOsAndBrowser(log.getUserAgent());
        log.setIpSource(ipSource);
        log.setOs(userAgentDTO.getOs());
        log.setBrowser(userAgentDTO.getBrowser());
        if(exceptionLogMapper.saveExceptionLog(log) != 1) {
            throw new PersistenceException("日志添加失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteExceptionLogById(Long id) {
        if(exceptionLogMapper.deleteExceptionLogById(id) != 1) {
            throw new PersistenceException("删除日志失败");
        }
    }
}
