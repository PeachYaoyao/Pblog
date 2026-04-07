package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.Moment;

import java.util.List;

@Mapper
public interface MomentMapper {
    List<Moment> getMomentList();
    int addLikeByMomentId(Long momentId);
    int updateMomentPublishedById(Long momentId, Boolean published);
    Moment getMomentById(Long id);
    int deleteMomentById(Long id);
    int saveMoment(Moment moment);
    int updateMoment(Moment moment);
}
