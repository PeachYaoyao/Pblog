package top.peachyao.service;

import org.springframework.scheduling.annotation.Async;
import top.peachyao.entity.Visitor;

public interface VisitorService {
    boolean hasUUID(String uuid);
    @Async
    void saveVisitor(Visitor visitor);
}
