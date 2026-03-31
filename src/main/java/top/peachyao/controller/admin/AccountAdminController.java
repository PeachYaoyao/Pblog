package top.peachyao.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.peachyao.entity.User;
import top.peachyao.handler.Result;
import top.peachyao.service.UserService;

/**
 * @Description: 账号后台管理
 * @Author: PeachYao
 * @Date: 2026-03-31
 */
@RestController
@RequestMapping("/admin")
public class AccountAdminController {
    @Autowired
    UserService userService;

    /**
     * 账号密码修改
     */
    @PostMapping("/account")
    public Result account(@RequestBody User user,
                          @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
        boolean res = userService.changeAccount(user, jwt);
        return res ? Result.ok("修改成功") : Result.ok("修改失败");
    }
}
