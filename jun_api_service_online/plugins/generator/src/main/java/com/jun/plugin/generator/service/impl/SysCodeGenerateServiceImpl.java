package com.jun.plugin.generator.service.impl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.plugin.generator.entity.SysCodeGenerateEntity;
import com.jun.plugin.generator.mapper.SysCodeGenerateMapper;
import com.jun.plugin.generator.service.SysCodeGenerateService;

/**
 * @description 代码生成基础配置服务层实现
 * @author Wujun
 * @date 2023-10-09
 */
@Service
public class SysCodeGenerateServiceImpl extends ServiceImpl<SysCodeGenerateMapper, SysCodeGenerateEntity> implements SysCodeGenerateService {

	@Autowired
    private SysCodeGenerateMapper sysCodeGenerateMapper;

}



