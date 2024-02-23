package com.jun.plugin.generator.controller;
import com.jun.plugin.generator.vo.SysCodeGenerateConfigVo;
import com.jun.plugin.generator.dto.SysCodeGenerateConfigDto;
import com.jun.plugin.generator.mapper.SysCodeGenerateConfigMapper;
import com.jun.plugin.generator.entity.SysCodeGenerateConfigEntity;
import com.jun.plugin.generator.service.SysCodeGenerateConfigService;
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
* @description 代码生成详细配置
* @author Wujun
* @date 2023-10-09
*/
@Api(tags = "代码生成详细配置-管理")
@Slf4j
@RestController
@RequestMapping("/sysCodeGenerateConfig")
public class SysCodeGenerateConfigController {

    @Resource
    private SysCodeGenerateConfigService sysCodeGenerateConfigService;
    
    @Resource
    private SysCodeGenerateConfigMapper sysCodeGenerateConfigMapper;
    
    @ApiOperation(value = "代码生成详细配置-新增")
    @PostMapping("/add")
    //@RequiresPermissions("sysCodeGenerateConfig:add")
    public Result add(@Validated(SysCodeGenerateConfigVo.Create.class) @RequestBody SysCodeGenerateConfigVo vo) {
    	SysCodeGenerateConfigDto dto = new SysCodeGenerateConfigDto();
    	BeanUtils.copyProperties(vo, dto);
        if (ObjectUtils.isEmpty(dto.getCodeGenId())) {
            return Result.fail("参数[codeGenId]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getColumnName())) {
            return Result.fail("参数[columnName]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getJavaName())) {
            return Result.fail("参数[javaName]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getDataType())) {
            return Result.fail("参数[dataType]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getColumnComment())) {
            return Result.fail("参数[columnComment]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getJavaType())) {
            return Result.fail("参数[javaType]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getEffectType())) {
            return Result.fail("参数[effectType]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getDictTypeCode())) {
            return Result.fail("参数[dictTypeCode]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getWhetherTable())) {
            return Result.fail("参数[whetherTable]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getWhetherAddUpdate())) {
            return Result.fail("参数[whetherAddUpdate]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getWhetherRetract())) {
            return Result.fail("参数[whetherRetract]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getWhetherRequired())) {
            return Result.fail("参数[whetherRequired]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getQueryWhether())) {
            return Result.fail("参数[queryWhether]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getQueryType())) {
            return Result.fail("参数[queryType]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getColumnKey())) {
            return Result.fail("参数[columnKey]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getColumnKeyName())) {
            return Result.fail("参数[columnKeyName]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getWhetherCommon())) {
            return Result.fail("参数[whetherCommon]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getCreateTime())) {
            return Result.fail("参数[createTime]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getCreateUser())) {
            return Result.fail("参数[createUser]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getUpdateTime())) {
            return Result.fail("参数[updateTime]不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getUpdateUser())) {
            return Result.fail("参数[updateUser]不能为空");
        }
        LambdaQueryWrapper<SysCodeGenerateConfigEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysCodeGenerateConfigEntity::getId, dto.getId());
        List<SysCodeGenerateConfigEntity> list = sysCodeGenerateConfigService.list(queryWrapper);
        if (list.size() > 0) {
            return Result.fail("数据已存在");
        }
        SysCodeGenerateConfigEntity entity = new SysCodeGenerateConfigEntity();
        
        BeanUtils.copyProperties(dto, entity);
        return Result.success(sysCodeGenerateConfigService.save(entity));
    }
    
    @ApiOperation(value = "代码生成详细配置-删除")
    @DeleteMapping("/remove")
    //@RequiresPermissions("sysCodeGenerateConfig:remove")
    public Result delete(@Validated(SysCodeGenerateConfigVo.Delete.class) @RequestBody SysCodeGenerateConfigVo vo) {
    	SysCodeGenerateConfigDto dto = new SysCodeGenerateConfigDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<SysCodeGenerateConfigEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysCodeGenerateConfigEntity::getId, dto.getId());
        return Result.success(sysCodeGenerateConfigService.remove(queryWrapper));
    }

    @ApiOperation(value = "代码生成详细配置-删除")
    @DeleteMapping("/delete")
    //@RequiresPermissions("sysCodeGenerateConfig:delete")
    public Result delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return Result.success(sysCodeGenerateConfigService.removeByIds(ids));
    }


    @ApiOperation(value = "代码生成详细配置-更新")
    @PutMapping("/update")
    //@RequiresPermissions("sysCodeGenerateConfig:update")
    public Result update(@Validated(SysCodeGenerateConfigVo.Update.class) @RequestBody SysCodeGenerateConfigVo vo) {
    	SysCodeGenerateConfigDto dto = new SysCodeGenerateConfigDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<SysCodeGenerateConfigEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysCodeGenerateConfigEntity::getId, dto.getId());
        SysCodeGenerateConfigEntity entity = sysCodeGenerateConfigService.getOne(queryWrapper);;
        if (entity == null) {
            //return Result.fail("数据不存在");
            entity = new SysCodeGenerateConfigEntity();
        }
        BeanUtils.copyProperties(dto, entity);
        return Result.success(sysCodeGenerateConfigService.saveOrUpdate(entity));
    }
    


    @ApiOperation(value = "代码生成详细配置-查询单条")
    @RequestMapping(value = "/getOne",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("sysCodeGenerateConfig:getOne")
    public Result getOne(@RequestBody SysCodeGenerateConfigVo vo) {
    	SysCodeGenerateConfigDto dto = new SysCodeGenerateConfigDto();
    	BeanUtils.copyProperties(vo, dto);
         if (ObjectUtils.isEmpty(dto.getId())) {
              return Result.fail("参数[id]不能为空");
         }
        LambdaQueryWrapper<SysCodeGenerateConfigEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysCodeGenerateConfigEntity::getId, dto.getId());
        SysCodeGenerateConfigEntity entity = sysCodeGenerateConfigService.getOne(queryWrapper);;
        return Result.success(entity);
    }
    
    


    @ApiOperation(value = "代码生成详细配置-查询列表分页数据")
    @RequestMapping(value = "/listByPage",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("sysCodeGenerateConfig:listByPage")
    public Result listByPage(@RequestBody SysCodeGenerateConfigVo sysCodeGenerateConfig) {
        Page page = new Page(sysCodeGenerateConfig.getPage(), sysCodeGenerateConfig.getLimit());
        SysCodeGenerateConfigDto dto = new SysCodeGenerateConfigDto();
    	BeanUtils.copyProperties(sysCodeGenerateConfig, dto);
        LambdaQueryWrapper<SysCodeGenerateConfigEntity> queryWrapper = Wrappers.lambdaQuery();
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getId())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getId, dto.getId());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getCodeGenId())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getCodeGenId, dto.getCodeGenId());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getColumnName())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getColumnName, dto.getColumnName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getJavaName())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getJavaName, dto.getJavaName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getDataType())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getDataType, dto.getDataType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getColumnComment())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getColumnComment, dto.getColumnComment());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getJavaType())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getJavaType, dto.getJavaType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getEffectType())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getEffectType, dto.getEffectType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getDictTypeCode())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getDictTypeCode, dto.getDictTypeCode());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherTable())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherTable, dto.getWhetherTable());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherAddUpdate())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherAddUpdate, dto.getWhetherAddUpdate());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherRetract())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherRetract, dto.getWhetherRetract());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherRequired())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherRequired, dto.getWhetherRequired());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getQueryWhether())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getQueryWhether, dto.getQueryWhether());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getQueryType())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getQueryType, dto.getQueryType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getColumnKey())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getColumnKey, dto.getColumnKey());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getColumnKeyName())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getColumnKeyName, dto.getColumnKeyName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherCommon())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherCommon, dto.getWhetherCommon());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getCreateTime())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getCreateTime, dto.getCreateTime());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getCreateUser())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getCreateUser, dto.getCreateUser());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getUpdateTime())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getUpdateTime, dto.getUpdateTime());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getUpdateUser())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getUpdateUser, dto.getUpdateUser());
        }
        IPage<SysCodeGenerateConfigEntity> iPage = sysCodeGenerateConfigService.page(page, queryWrapper);
        return Result.success(iPage);
    }
    
    @ApiOperation(value = "代码生成详细配置-查询全部列表数据")
    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST})
    //@RequiresPermissions("sysCodeGenerateConfig:list")
    public Result findListByPage(@RequestBody SysCodeGenerateConfigVo sysCodeGenerateConfig) {
        LambdaQueryWrapper<SysCodeGenerateConfigEntity> queryWrapper = Wrappers.lambdaQuery();
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getId())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getId, sysCodeGenerateConfig.getId());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getCodeGenId())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getCodeGenId, sysCodeGenerateConfig.getCodeGenId());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getColumnName())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getColumnName, sysCodeGenerateConfig.getColumnName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getJavaName())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getJavaName, sysCodeGenerateConfig.getJavaName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getDataType())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getDataType, sysCodeGenerateConfig.getDataType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getColumnComment())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getColumnComment, sysCodeGenerateConfig.getColumnComment());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getJavaType())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getJavaType, sysCodeGenerateConfig.getJavaType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getEffectType())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getEffectType, sysCodeGenerateConfig.getEffectType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getDictTypeCode())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getDictTypeCode, sysCodeGenerateConfig.getDictTypeCode());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherTable())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherTable, sysCodeGenerateConfig.getWhetherTable());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherAddUpdate())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherAddUpdate, sysCodeGenerateConfig.getWhetherAddUpdate());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherRetract())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherRetract, sysCodeGenerateConfig.getWhetherRetract());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherRequired())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherRequired, sysCodeGenerateConfig.getWhetherRequired());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getQueryWhether())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getQueryWhether, sysCodeGenerateConfig.getQueryWhether());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getQueryType())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getQueryType, sysCodeGenerateConfig.getQueryType());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getColumnKey())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getColumnKey, sysCodeGenerateConfig.getColumnKey());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getColumnKeyName())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getColumnKeyName, sysCodeGenerateConfig.getColumnKeyName());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getWhetherCommon())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getWhetherCommon, sysCodeGenerateConfig.getWhetherCommon());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getCreateTime())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getCreateTime, sysCodeGenerateConfig.getCreateTime());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getCreateUser())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getCreateUser, sysCodeGenerateConfig.getCreateUser());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getUpdateTime())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getUpdateTime, sysCodeGenerateConfig.getUpdateTime());
        }
        if (!ObjectUtils.isEmpty(sysCodeGenerateConfig.getUpdateUser())) {
            queryWrapper.eq(SysCodeGenerateConfigEntity::getUpdateUser, sysCodeGenerateConfig.getUpdateUser());
        }
        List<SysCodeGenerateConfigEntity> list = sysCodeGenerateConfigService.list(queryWrapper);
        return Result.success(list);
    }


}

