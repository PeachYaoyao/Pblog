package top.peachyao.service;

import top.peachyao.entity.Moment;

import java.util.List;

public interface MomentService {
    List<Moment> getMomentList();
    List<Moment> getMomentVOList(Integer pageNum, String jwt);
    void addLikeByMomentId(Long momentId);
    void updateMomentPublishedById(Long id, Boolean published);
    Moment getMomentById(Long id);
    void deleteMomentById(Long id);
    void saveMoment(Moment moment);
    void updateMoment(Moment moment);
}
