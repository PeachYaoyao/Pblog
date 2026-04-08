package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.Visitor;

/**
 * @Description: 访客统计持久层接口
 * @Author: PeachYao
 * @Date: 2026-04-08
 */
@Mapper
public interface VisitorMapper {
    int hasUUID(String uuid);
    int saveVisitor(Visitor visitor);
}
