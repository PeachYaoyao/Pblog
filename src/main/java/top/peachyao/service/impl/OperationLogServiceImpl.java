package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.entity.OperationLog;
import top.peachyao.exception.PersistenceException;
import top.peachyao.mapper.OperationLogMapper;
import top.peachyao.model.dto.UserAgentDTO;
import top.peachyao.service.OperationLogService;
import top.peachyao.util.IpAddressUtils;
import top.peachyao.util.UserAgentUtils;

import java.util.List;

/**
 * @Description: 操作日志业务层实现
 * @Author: PeachYao
 * @Date: 2026-04-07
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    OperationLogMapper operationLogMapper;
    @Autowired
    UserAgentUtils userAgentUtils;
    @Override
    public List<OperationLog> getOperationLogListByDate(String startDate, String endDate) {
        return operationLogMapper.getOperationLogListByDate(startDate, endDate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOperationLog(OperationLog log) {
        String ipSource = IpAddressUtils.getCityInfo(log.getIp());
        UserAgentDTO userAgentDTO = userAgentUtils.parseOsAndBrowser(log.getUserAgent());
        log.setIpSource(ipSource);
        log.setOs(userAgentDTO.getOs());
        log.setBrowser(userAgentDTO.getBrowser());
        if (operationLogMapper.saveOperationLog(log) != 1) {
            throw new PersistenceException("日志添加失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOperationLogById(Long id) {
        if(operationLogMapper.deleteOperationLogById(id) != 1) {
            throw new PersistenceException("删除日志失败");
        }
    }
}
