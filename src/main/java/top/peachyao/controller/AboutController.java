package top.peachyao.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.peachyao.handler.Result;
import top.peachyao.service.AboutService;

import java.util.Map;

/**
 * @Description: 关于我页面
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@RestController
public class AboutController {
    @Autowired
    private AboutService aboutService;

    /**
     * 获取关于我页面信息
     *
     * @return
     */
    @GetMapping("/about")
    public Result getABoutInfoMap() {
        return Result.ok("请求成功", aboutService.getAboutInfo());
    }
}
