package top.peachyao.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.constant.JwtConstants;
import top.peachyao.entity.Moment;
import top.peachyao.entity.User;
import top.peachyao.exception.NotFoundException;
import top.peachyao.exception.PersistenceException;
import top.peachyao.mapper.MomentMapper;
import top.peachyao.service.MomentService;
import top.peachyao.util.JwtUtils;
import top.peachyao.util.markdown.MarkdownUtils;

import java.awt.print.PrinterAbortException;
import java.util.List;

@Service
public class MomentServiceImpl implements MomentService {
    @Autowired
    MomentMapper momentMapper;
    @Autowired
    UserServiceImpl userService;
    //每页显示5条动态
    private static final int pageSize = 5;
    //动态列表排序方式
    private static final String orderBy = "create_time desc";
    //私密动态提示
    private static final String PRIVATE_MOMENT_CONTENT = "<p>此条为私密动态，仅发布者可见！</p>";

    @Override
    public List<Moment> getMomentList() {
        return momentMapper.getMomentList();
    }

    @Override
    public List<Moment> getMomentVOList(Integer pageNum, String jwt) {
        boolean adminIdentity = false;
        if(JwtUtils.judgeTokenIsExist(jwt)) {
            try {
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                if(subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
                    String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                    User admin = (User) userService.loadUserByUsername(username);
                    if(admin != null) {
                        adminIdentity = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Moment> moments = momentMapper.getMomentList();
        for(Moment moment : moments) {
            if(adminIdentity || moment.getPublished()) {
                moment.setContent(MarkdownUtils.toHtmlWithExtensions(moment.getContent()));
            } else {
                moment.setContent(PRIVATE_MOMENT_CONTENT);
            }
        }
        return moments;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addLikeByMomentId(Long momentId) {
        if(momentMapper.addLikeByMomentId(momentId) != 1) {
            throw new PersistenceException("操作失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMomentPublishedById(Long id, Boolean published) {
        if(momentMapper.updateMomentPublishedById(id, published) != 1) {
            throw new PersistenceException("操作失败");
        }
    }

    @Override
    public Moment getMomentById(Long id) {
        Moment moment = momentMapper.getMomentById(id);
        if(moment == null) {
            throw new NotFoundException("动态不存在");
        }
        return moment;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMomentById(Long id) {
        if(momentMapper.deleteMomentById(id) != 1) {
            throw new PersistenceException("删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMoment(Moment moment) {
        if(momentMapper.saveMoment(moment) != 1) {
            throw new PersistenceException("动态添加失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMoment(Moment moment) {
        if(momentMapper.updateMoment(moment) != 1) {
            throw new PersistenceException("动态修改失败");
        }
    }
}
