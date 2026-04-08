package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.entity.Visitor;
import top.peachyao.exception.PersistenceException;
import top.peachyao.mapper.VisitorMapper;
import top.peachyao.model.dto.UserAgentDTO;
import top.peachyao.service.RedisService;
import top.peachyao.service.VisitorService;
import top.peachyao.util.IpAddressUtils;
import top.peachyao.util.UserAgentUtils;

/**
 * @Description: 访客统计业务层实现
 * @Author: PeachYao
 * @Date: 2026-04-08
 */
@Service
public class VisitorServiceImpl implements VisitorService {
    @Autowired
    VisitorMapper visitorMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    UserAgentUtils userAgentUtils;

    @Override
    public boolean hasUUID(String uuid) {
        return visitorMapper.hasUUID(uuid) != 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveVisitor(Visitor visitor) {
        String ipSource = IpAddressUtils.getCityInfo(visitor.getIp());
        UserAgentDTO userAgentDTO = userAgentUtils.parseOsAndBrowser(visitor.getUserAgent());
        visitor.setIpSource(ipSource);
        visitor.setOs(userAgentDTO.getOs());
        visitor.setBrowser(userAgentDTO.getBrowser());
        if(visitorMapper.saveVisitor(visitor) != 1) {
            throw new PersistenceException("访客添加失败");
        }
    }
}
