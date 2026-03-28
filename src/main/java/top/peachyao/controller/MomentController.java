package top.peachyao.controller;


import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.peachyao.entity.Moment;
import top.peachyao.handler.Result;
import top.peachyao.model.vo.PageResult;
import top.peachyao.service.MomentService;

/**
 * @Description: 动态
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@RestController
public class MomentController {
    @Autowired
    MomentService momentService;

    /**
     * 分页查询动态List
     *
     * @param pageNum 页码
     * @param jwt     博主访问Token
     * @return
     */
    @GetMapping("/moments")
    public Result moments(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
        PageInfo<Moment> pageInfo = new PageInfo<>(momentService.getMomentVOList(pageNum, jwt));
        PageResult<Moment> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
        return Result.ok("获取成功", pageResult);
    }

    /**
     * 给动态点赞
     * 简单限制一下点赞
     *
     * @param id 动态id
     * @return
     */
    @PostMapping("/moment/like/{id}")
    public Result like(@PathVariable Long id) {
        momentService.addLikeByMomentId(id);
        return Result.ok("点赞成功");
    }
}
