package com.bjc.lcp.app1.service.impl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjc.lcp.app1.entity.ExtSalgradeEntity;
import com.bjc.lcp.app1.mapper.ExtSalgradeMapper;
import com.bjc.lcp.app1.service.ExtSalgradeService;

/**
 * @description 服务层实现
 * @author Wujun
 * @date 2024-01-25
 */
@Service
public class ExtSalgradeServiceImpl extends ServiceImpl<ExtSalgradeMapper, ExtSalgradeEntity> implements ExtSalgradeService {

	@Autowired
    private ExtSalgradeMapper extSalgradeMapper;

}



