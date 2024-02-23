package com.jun.plugin.generator.service.impl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.plugin.generator.entity.SysCodeGenerateConfigEntity;
import com.jun.plugin.generator.mapper.SysCodeGenerateConfigMapper;
import com.jun.plugin.generator.service.SysCodeGenerateConfigService;

/**
 * @description 代码生成详细配置服务层实现
 * @author Wujun
 * @date 2023-10-09
 */
@Service
public class SysCodeGenerateConfigServiceImpl extends ServiceImpl<SysCodeGenerateConfigMapper, SysCodeGenerateConfigEntity> implements SysCodeGenerateConfigService {

	@Autowired
    private SysCodeGenerateConfigMapper sysCodeGenerateConfigMapper;

}



