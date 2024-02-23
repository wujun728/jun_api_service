package com.bjc.lcp.app1.controller;
import com.bjc.lcp.app1.vo.ExtSalgradeVo;
import com.bjc.lcp.app1.dto.ExtSalgradeDto;
import com.bjc.lcp.app1.mapper.ExtSalgradeMapper;
import com.bjc.lcp.app1.entity.ExtSalgradeEntity;
import com.bjc.lcp.app1.service.ExtSalgradeService;
//import com.bjc.lcp.common.cnt.enums.CntTableNameEnum;
//import com.bjc.lcp.common.cnt.service.CntService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.servlet.ModelAndView;
import com.jun.plugin.common.Result;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
* @description 
* @author Wujun
* @date 2024-01-25
*/
@Api(tags = "-管理")
@Slf4j
@RestController
@RequestMapping("/extSalgrade")
public class ExtSalgradeController {

    @Resource
    private ExtSalgradeService extSalgradeService;
    
    @Resource
    private ExtSalgradeMapper extSalgradeMapper;
    
    @ApiOperation(value = "-新增")
    @PostMapping("/add")
    //@RequiresPermissions("extSalgrade:add")
    public Result add(@Validated(ExtSalgradeVo.Create.class) @RequestBody ExtSalgradeVo vo) {
    	ExtSalgradeDto dto = new ExtSalgradeDto();
    	BeanUtils.copyProperties(vo, dto);
        if (ObjectUtils.isEmpty(dto.getGrade())) {
            return Result.fail("参数[grade]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getLosal())) {
            return Result.fail("参数[losal]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getHisal())) {
            return Result.fail("参数[hisal]不能为空");
        }
        LambdaQueryWrapper<ExtSalgradeEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ExtSalgradeEntity::getId, dto.getId());
        List<ExtSalgradeEntity> list = extSalgradeService.list(queryWrapper);
        if (list.size() > 0) {
            return Result.fail("数据已存在");
        }
        ExtSalgradeEntity entity = new ExtSalgradeEntity();
        
        BeanUtils.copyProperties(dto, entity);
        return Result.success(extSalgradeService.save(entity));
    }
    
    @ApiOperation(value = "-删除")
    @DeleteMapping("/remove")
    //@RequiresPermissions("extSalgrade:remove")
    public Result delete(@Validated(ExtSalgradeVo.Delete.class) @RequestBody ExtSalgradeVo vo) {
    	ExtSalgradeDto dto = new ExtSalgradeDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<ExtSalgradeEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ExtSalgradeEntity::getId, dto.getId());
        return Result.success(extSalgradeService.remove(queryWrapper));
    }

    @ApiOperation(value = "-删除")
    @DeleteMapping("/delete")
    //@RequiresPermissions("extSalgrade:delete")
    public Result delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return Result.success(extSalgradeService.removeByIds(ids));
    }


    @ApiOperation(value = "-更新")
    @PutMapping("/update")
    //@RequiresPermissions("extSalgrade:update")
    public Result update(@Validated(ExtSalgradeVo.Update.class) @RequestBody ExtSalgradeVo vo) {
    	ExtSalgradeDto dto = new ExtSalgradeDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<ExtSalgradeEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ExtSalgradeEntity::getId, dto.getId());
        ExtSalgradeEntity entity = extSalgradeService.getOne(queryWrapper);;
        if (entity == null) {
            //return Result.fail("数据不存在");
            entity = new ExtSalgradeEntity();
        }
        BeanUtils.copyProperties(dto, entity);
        return Result.success(extSalgradeService.saveOrUpdate(entity));
    }
    


    @ApiOperation(value = "-查询单条")
    @RequestMapping(value = "/getOne",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("extSalgrade:getOne")
    public Result getOne(@RequestBody ExtSalgradeVo vo) {
    	ExtSalgradeDto dto = new ExtSalgradeDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<ExtSalgradeEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ExtSalgradeEntity::getId, dto.getId());
        ExtSalgradeEntity entity = extSalgradeService.getOne(queryWrapper);;
        return Result.success(entity);
    }
    
    


    @ApiOperation(value = "-查询列表分页数据")
    @RequestMapping(value = "/listByPage",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("extSalgrade:listByPage")
    public Result listByPage(@RequestBody ExtSalgradeVo extSalgrade) {
        Page page = new Page(extSalgrade.getPage(), extSalgrade.getLimit());
        ExtSalgradeDto dto = new ExtSalgradeDto();
    	BeanUtils.copyProperties(extSalgrade, dto);
        LambdaQueryWrapper<ExtSalgradeEntity> queryWrapper = Wrappers.lambdaQuery();
        if (!ObjectUtils.isEmpty(extSalgrade.getGrade())) {
            queryWrapper.eq(ExtSalgradeEntity::getGrade, dto.getGrade());
        }
        if (!ObjectUtils.isEmpty(extSalgrade.getLosal())) {
            queryWrapper.eq(ExtSalgradeEntity::getLosal, dto.getLosal());
        }
        if (!ObjectUtils.isEmpty(extSalgrade.getHisal())) {
            queryWrapper.eq(ExtSalgradeEntity::getHisal, dto.getHisal());
        }
        if (!ObjectUtils.isEmpty(extSalgrade.getId())) {
            queryWrapper.eq(ExtSalgradeEntity::getId, dto.getId());
        }
        IPage<ExtSalgradeEntity> iPage = extSalgradeService.page(page, queryWrapper);
        return Result.success(iPage);
    }
    
    @ApiOperation(value = "-查询全部列表数据")
    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("extSalgrade:list")
    public Result findListByPage(@RequestBody ExtSalgradeVo extSalgrade) {
        LambdaQueryWrapper<ExtSalgradeEntity> queryWrapper = Wrappers.lambdaQuery();
        if (!ObjectUtils.isEmpty(extSalgrade.getGrade())) {
            queryWrapper.eq(ExtSalgradeEntity::getGrade, extSalgrade.getGrade());
        }
        if (!ObjectUtils.isEmpty(extSalgrade.getLosal())) {
            queryWrapper.eq(ExtSalgradeEntity::getLosal, extSalgrade.getLosal());
        }
        if (!ObjectUtils.isEmpty(extSalgrade.getHisal())) {
            queryWrapper.eq(ExtSalgradeEntity::getHisal, extSalgrade.getHisal());
        }
        if (!ObjectUtils.isEmpty(extSalgrade.getId())) {
            queryWrapper.eq(ExtSalgradeEntity::getId, extSalgrade.getId());
        }
        List<ExtSalgradeEntity> list = extSalgradeService.list(queryWrapper);
        return Result.success(list);
    }


}

