package top.peachyao.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.peachyao.handler.Result;
import top.peachyao.service.AboutService;

import java.util.Map;

/**
 * @Description: 关于我页面后台管理
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@RestController
@RequestMapping("/admin")
public class AboutAdminController {
    @Autowired
    AboutService aboutService;

    /**
     * 获取关于我页面配置
     *
     * @return
     */
    @GetMapping("/about")
    public Result getAboutSetting() {
        return Result.ok("请求成功",aboutService.getAboutSetting());
    }

    /**
     * 修改关于我页面
     *
     * @param map
     * @return
     */
    @PutMapping("/about")
    public Result updateAbout(@RequestBody Map<String, String> map) {
        aboutService.updateAbout(map);
        return Result.ok("修改成功");
    }
}
