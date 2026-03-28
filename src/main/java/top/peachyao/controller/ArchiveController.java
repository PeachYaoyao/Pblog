package top.peachyao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.peachyao.handler.Result;
import top.peachyao.service.BlogService;

import java.util.Map;
import java.util.Objects;

/**
 * @Description: 归档页面
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@RestController
public class ArchiveController {
    @Autowired
    BlogService blogService;
    /**
     * 按年月分组归档公开博客 统计公开博客总数
     *
     * @return
     */
    @GetMapping("/archives")
    public Result archives() {
        Map<String, Object> archiveBlogMap = blogService.getArchiveBlogAndCountByIsPublished();
        return Result.ok("请求成功", archiveBlogMap);
    }
}
