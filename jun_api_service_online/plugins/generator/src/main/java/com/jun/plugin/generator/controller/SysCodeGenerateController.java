package com.jun.plugin.generator.controller;
import com.jun.plugin.generator.vo.SysCodeGenerateVo;
import com.jun.plugin.generator.dto.SysCodeGenerateDto;
import com.jun.plugin.generator.mapper.SysCodeGenerateMapper;
import com.jun.plugin.generator.entity.SysCodeGenerateEntity;
import com.jun.plugin.generator.service.SysCodeGenerateService;
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
* @Version666
* @description 代码生成基础配置
* @author Wujun
* @date 2023-10-09
*/
@Api(tags = "代码生成基础配置-管理")
@Slf4j
@RestController
@RequestMapping("/sysCodeGenerate")
public class SysCodeGenerateController {

    @Resource
    private SysCodeGenerateService sysCodeGenerateService;
    
    @Resource
    private SysCodeGenerateMapper sysCodeGenerateMapper;
    
    @ApiOperation(value = "代码生成基础配置-新增")
    @PostMapping("/add")
    //@RequiresPermissions("sysCodeGenerate:add")
    public Result add(@Validated(SysCodeGenerateVo.Create.class) @RequestBody SysCodeGenerateVo vo) {
    	SysCodeGenerateDto dto = new SysCodeGenerateDto();
    	BeanUtils.copyProperties(vo, dto);
        if (ObjectUtils.isEmpty(dto.getPackageName())) {
            return Result.fail("参数[packageName]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getBuname())) {
            return Result.fail("参数[buname]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getTableComment())) {
            return Result.fail("参数[tableComment]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getAppCode())) {
            return Result.fail("参数[appCode]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getMenuPid())) {
            return Result.fail("参数[menuPid]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getCreateUser())) {
            return Result.fail("参数[createUser]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getCreateTime())) {
            return Result.fail("参数[createTime]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getUpdateUser())) {
            return Result.fail("参数[updateUser]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getUpdateTime())) {
            return Result.fail("参数[updateTime]不能为空");
        }
        LambdaQueryWrapper<SysCodeGenerateEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysCodeGenerateEntity::getId, dto.getId());
        List<SysCodeGenerateEntity> list = sysCodeGenerateService.list(queryWrapper);
        if (list.size() > 0) {
            return Result.fail("数据已存在");
        }
        SysCodeGenerateEntity entity = new SysCodeGenerateEntity();
        
        BeanUtils.copyProperties(dto, entity);
        return Result.success(sysCodeGenerateService.save(entity));
    }
    
    @ApiOperation(value = "代码生成基础配置-删除")
    @DeleteMapping("/remove")
    //@RequiresPermissions("sysCodeGenerate:remove")
    public Result delete(@Validated(SysCodeGenerateVo.Delete.class) @RequestBody SysCodeGenerateVo vo) {
    	SysCodeGenerateDto dto = new SysCodeGenerateDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<SysCodeGenerateEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysCodeGenerateEntity::getId, dto.getId());
        return Result.success(sysCodeGenerateService.remove(queryWrapper));
    }

    @ApiOperation(value = "代码生成基础配置-删除")
    @DeleteMapping("/delete")
    //@RequiresPermissions("sysCodeGenerate:delete")
    public Result delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return Result.success(sysCodeGenerateService.removeByIds(ids));
    }


    @ApiOperation(value = "代码生成基础配置-更新")
    @PutMapping("/update")
    //@RequiresPermissions("sysCodeGenerate:update")
    public Result update(@Validated(SysCodeGenerateVo.Update.class) @RequestBody SysCodeGenerateVo vo) {
    	SysCodeGenerateDto dto = new SysCodeGenerateDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<SysCodeGenerateEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysCodeGenerateEntity::getId, dto.getId());
        SysCodeGenerateEntity entity = sysCodeGenerateService.getOne(queryWrapper);;
        if (entity == null) {
            //return Result.fail("数据不存在");
            entity = new SysCodeGenerateEntity();
        }
        BeanUtils.copyProperties(dto, entity);
        return Result.success(sysCodeGenerateService.saveOrUpdate(entity));
    }
    


    @ApiOperation(value = "代码生成基础配置-查询单条")
    @RequestMapping(value = "/getOne",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("sysCodeGenerate:getOne")
    public Result getOne(@RequestBody SysCodeGenerateVo vo) {
    	SysCodeGenerateDto dto = new SysCodeGenerateDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<SysCodeGenerateEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysCodeGenerateEntity::getId, dto.getId());
        SysCodeGenerateEntity entity = sysCodeGenerateService.getOne(queryWrapper);;
        return Result.success(entity);
    }
    
    


    @ApiOperation(value = "代码生成基础配置-查询列表分页数据")
    @RequestMapping(value = "/listByPage",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("sysCodeGenerate:listByPage")
    public Result listByPage(@RequestBody SysCodeGenerateVo sysCodeGenerate) {
        Page page = new Page(sysCodeGenerate.getPage(), sysCodeGenerate.getLimit());
        SysCodeGenerateDto dto = new SysCodeGenerateDto();
    	BeanUtils.copyProperties(sysCodeGenerate, dto);
        LambdaQueryWrapper<SysCodeGenerateEntity> queryWrapper = Wrappers.lambdaQuery();
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getId())) {
            queryWrapper.eq(SysCodeGenerateEntity::getId, dto.getId());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getAuthorName())) {
            queryWrapper.eq(SysCodeGenerateEntity::getAuthorName, dto.getAuthorName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getClasname())) {
            queryWrapper.eq(SysCodeGenerateEntity::getClasname, dto.getClasname());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getTablePrefix())) {
            queryWrapper.eq(SysCodeGenerateEntity::getTablePrefix, dto.getTablePrefix());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getGenerateType())) {
            queryWrapper.eq(SysCodeGenerateEntity::getGenerateType, dto.getGenerateType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getTableName())) {
            queryWrapper.eq(SysCodeGenerateEntity::getTableName, dto.getTableName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getPackageName())) {
            queryWrapper.eq(SysCodeGenerateEntity::getPackageName, dto.getPackageName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getBuname())) {
            queryWrapper.eq(SysCodeGenerateEntity::getBuname, dto.getBuname());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getTableComment())) {
            queryWrapper.eq(SysCodeGenerateEntity::getTableComment, dto.getTableComment());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getAppCode())) {
            queryWrapper.eq(SysCodeGenerateEntity::getAppCode, dto.getAppCode());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getMenuPid())) {
            queryWrapper.eq(SysCodeGenerateEntity::getMenuPid, dto.getMenuPid());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getCreateUser())) {
            queryWrapper.eq(SysCodeGenerateEntity::getCreateUser, dto.getCreateUser());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getCreateTime())) {
            queryWrapper.eq(SysCodeGenerateEntity::getCreateTime, dto.getCreateTime());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getUpdateUser())) {
            queryWrapper.eq(SysCodeGenerateEntity::getUpdateUser, dto.getUpdateUser());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getUpdateTime())) {
            queryWrapper.eq(SysCodeGenerateEntity::getUpdateTime, dto.getUpdateTime());
        }
        IPage<SysCodeGenerateEntity> iPage = sysCodeGenerateService.page(page, queryWrapper);
        return Result.success(iPage);
    }
    
    @ApiOperation(value = "代码生成基础配置-查询全部列表数据")
    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("sysCodeGenerate:list")
    public Result findListByPage(@RequestBody SysCodeGenerateVo sysCodeGenerate) {
        LambdaQueryWrapper<SysCodeGenerateEntity> queryWrapper = Wrappers.lambdaQuery();
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getId())) {
            queryWrapper.eq(SysCodeGenerateEntity::getId, sysCodeGenerate.getId());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getAuthorName())) {
            queryWrapper.eq(SysCodeGenerateEntity::getAuthorName, sysCodeGenerate.getAuthorName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getClasname())) {
            queryWrapper.eq(SysCodeGenerateEntity::getClasname, sysCodeGenerate.getClasname());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getTablePrefix())) {
            queryWrapper.eq(SysCodeGenerateEntity::getTablePrefix, sysCodeGenerate.getTablePrefix());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getGenerateType())) {
            queryWrapper.eq(SysCodeGenerateEntity::getGenerateType, sysCodeGenerate.getGenerateType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getTableName())) {
            queryWrapper.eq(SysCodeGenerateEntity::getTableName, sysCodeGenerate.getTableName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getPackageName())) {
            queryWrapper.eq(SysCodeGenerateEntity::getPackageName, sysCodeGenerate.getPackageName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getBuname())) {
            queryWrapper.eq(SysCodeGenerateEntity::getBuname, sysCodeGenerate.getBuname());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getTableComment())) {
            queryWrapper.eq(SysCodeGenerateEntity::getTableComment, sysCodeGenerate.getTableComment());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getAppCode())) {
            queryWrapper.eq(SysCodeGenerateEntity::getAppCode, sysCodeGenerate.getAppCode());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getMenuPid())) {
            queryWrapper.eq(SysCodeGenerateEntity::getMenuPid, sysCodeGenerate.getMenuPid());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getCreateUser())) {
            queryWrapper.eq(SysCodeGenerateEntity::getCreateUser, sysCodeGenerate.getCreateUser());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getCreateTime())) {
            queryWrapper.eq(SysCodeGenerateEntity::getCreateTime, sysCodeGenerate.getCreateTime());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getUpdateUser())) {
            queryWrapper.eq(SysCodeGenerateEntity::getUpdateUser, sysCodeGenerate.getUpdateUser());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerate.getUpdateTime())) {
            queryWrapper.eq(SysCodeGenerateEntity::getUpdateTime, sysCodeGenerate.getUpdateTime());
        }
        List<SysCodeGenerateEntity> list = sysCodeGenerateService.list(queryWrapper);
        return Result.success(list);
    }


}

