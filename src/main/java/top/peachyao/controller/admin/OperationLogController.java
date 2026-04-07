package top.peachyao.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.peachyao.entity.LoginLog;
import top.peachyao.entity.OperationLog;
import top.peachyao.handler.Result;
import top.peachyao.service.LoginLogService;
import top.peachyao.service.OperationLogService;

/**
 * @Description: 登录日志后台管理
 * @Author: PeachYao
 * @Date: 2026-04-07
 */
@RestController
@RequestMapping("/admin")
public class OperationLogController {
    @Autowired
    OperationLogService operationLogService;

    /**
     * 分页查询异常日志列表
     *
     * @param date     按操作时间查询
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return
     */
    @GetMapping("/operationLogs")
    public Result operationLogs(@RequestParam(defaultValue = "") String[] date,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if(date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        String orderBy = "create_time desc";
        PageHelper.startPage(pageNum, pageSize, orderBy);
        PageInfo<OperationLog> pageInfo = new PageInfo<>(operationLogService.getOperationLogListByDate(startDate, endDate));
        return Result.ok("请求成功", pageInfo);
    }

    /**
     * 按id删除异常日志
     *
     * @param id 日志id
     * @return
     */
    @DeleteMapping("/operationLog")
    public Result delete(@RequestParam Long id) {
        operationLogService.deleteOperationLogById(id);
        return Result.ok("删除成功");
    }
}
