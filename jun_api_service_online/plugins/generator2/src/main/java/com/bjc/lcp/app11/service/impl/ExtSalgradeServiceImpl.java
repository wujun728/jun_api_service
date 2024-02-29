package com.bjc.lcp.app11.service.impl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjc.lcp.app11.entity.ExtSalgradeEntity;
import com.bjc.lcp.app11.mapper.ExtSalgradeMapper;
import com.bjc.lcp.app11.service.ExtSalgradeService;

/**
 * @description 服务层实现
 * @author Wujun
 * @date 2024-02-28
 */
@Service
public class ExtSalgradeServiceImpl extends ServiceImpl<ExtSalgradeMapper, ExtSalgradeEntity> implements ExtSalgradeService {

	@Autowired
    private ExtSalgradeMapper extSalgradeMapper;

}



