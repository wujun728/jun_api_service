package com.websql.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.websql.model.AjaxResult;
import com.websql.model.SysDetectionLogsModel;
import com.websql.model.SysDetectionModel;
import com.websql.service.DetectionService;
import com.websql.task.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DetectionManagerController
 * @Description 检测任务管理
 * @Author rabbit boy_0214@sina.com
 * @Date 2024-10-12  16:17
 **/
@Controller
@RequestMapping("/detectionManager")
@Slf4j
public class DetectionManagerController {

    @Resource
    private DetectionService detectionService;


    @RequestMapping("/listPage")
    public String listPage() {
        return "detectionListPage";
    }

    @RequestMapping("/addPage")
    public String addPage() {
        return "detectionAddPage";
    }

    @RequestMapping("/reportPage")
    public ModelAndView reportPage(Long id) {
        ModelAndView modelAndView = new ModelAndView("detectionReportPage");
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    /***
     * 加载执行计划列表
     * @param model
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public AjaxResult list(SysDetectionModel model) {
        return AjaxResult.success(detectionService.list(model));
    }


    /**
     * 新增执行计划
     *
     * @param model
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public AjaxResult add(@RequestBody SysDetectionModel model) {
        if (StpUtil.hasRole("demo-admin")) {
            return AjaxResult.error("抱歉,演示角色不允许创建检测任务!");
        }
        if (ObjectUtil.isEmpty(model.getName())) {
            return AjaxResult.error("检测任务名称不能为空哦!");
        }
        if (ObjectUtil.isEmpty(model.getCron())) {
            return AjaxResult.error("Cron表达式不能为空哦!");
        }
        if (ObjectUtil.isEmpty(model.getSqlContent())) {
            return AjaxResult.error("SQL内容不能为空哦!");
        }
        if (ObjectUtil.isEmpty(model.getDataBaseName())) {
            return AjaxResult.error("数据源不能为空哦!");
        }
        Long id = null;
        try {
            id = detectionService.add(model).getId();
            ScheduleUtils.removeDetectionTask(id);
            ScheduleUtils.addDetectionTask(model.getCron(), id, model.getName());
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            if (ObjectUtil.isNotNull(id)) {
                detectionService.deleteById(id);
            }
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 操作单条数据状态
     *
     * @param type 类型
     * @param id   主键
     * @param cron 定时时间
     * @return
     */
    @RequestMapping("/executeOnce/{type}/{id}")
    @ResponseBody
    public AjaxResult executeOnce(@PathVariable("type") int type, @PathVariable("id") Long id, String cron) {
        try {
            SysDetectionModel models = new SysDetectionModel();
            models.setId(id);
            SysDetectionModel vo = detectionService.selectById(id);
            ScheduleUtils.removeDetectionTask(id);
            switch (type) {
                case 1:
                    vo.setState("停止监控");
                    detectionService.updateById(vo);
                    break;
                case 2:
                    ScheduleUtils.addDetectionTask(vo.getCron(), id, vo.getName());
                    vo.setState("开始监控");
                    detectionService.updateById(vo);
                    break;
                case 3:
                    vo.setState("开始监控");
                    vo.setCron(cron);
                    ScheduleUtils.addDetectionTask(vo.getCron(), id, vo.getName());
                    detectionService.updateById(vo);
                    break;
                case 4:
                    detectionService.deleteById(id);
                    break;
                case 5:
                    ScheduleUtils.addDetectionTask(vo.getCron(), id, vo.getName());
                    break;
                default:
                    throw new RuntimeException("错误的类型!" + type);
            }
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /***
     * 加载下拉任务
     * @param activeId 默认选中的id
     * @return
     */
    @RequestMapping("/findDataSelect")
    @ResponseBody
    public Map findDataSourceList(@RequestParam(required = false) String activeId) {
        Map result = new HashMap(2);
        result.put("code", 0);
        result.put("data", detectionService.selectAllByActiveId(activeId));
        return result;
    }

    /***
     * 加载执行计划日志
     * @param model
     * @return
     */
    @RequestMapping("/logList")
    @ResponseBody
    public AjaxResult logList(SysDetectionLogsModel model) {
        //如果从默认页面进入则不查询指定taskId
        if (ObjectUtil.equal(-1L, model.getTaskId())) {
            model.setTaskId(null);
        }
        return AjaxResult.success(detectionService.logList(model));
    }

    /**
     * 日志报表
     *
     * @param model
     * @return
     */
    @RequestMapping("/logCharts")
    @ResponseBody
    public AjaxResult logCharts(@RequestBody SysDetectionLogsModel model) {
        //如果从默认页面进入则不查询指定taskId
        if (ObjectUtil.equal(-1L, model.getTaskId())) {
            model.setTaskId(null);
        }
        return AjaxResult.success(detectionService.logCharts(model));
    }

    /**
     * 删除日志记录
     * @param id
     * @return
     */
    @RequestMapping("/deleteLog")
    @ResponseBody
    public AjaxResult deleteLog(@RequestParam Long id) {
        if (ObjectUtil.isNull(id)) {
            return AjaxResult.error("必填参数为空!");
        }
        detectionService.deleteLog(id);
        return AjaxResult.success();
    }

    @RequestMapping("/deleteLogAll")
    @ResponseBody
    public AjaxResult deleteLogAll() {
        detectionService.deleteLogAll();
        return AjaxResult.success();
    }


}
