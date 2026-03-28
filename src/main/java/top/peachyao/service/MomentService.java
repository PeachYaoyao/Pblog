package top.peachyao.service;

import top.peachyao.entity.Moment;

import java.util.List;

public interface MomentService {
    List<Moment> getMomentVOList(Integer pageNum, String jwt);
    void addLikeByMomentId(Long momentId);
}
