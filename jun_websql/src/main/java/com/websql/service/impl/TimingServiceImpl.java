package com.websql.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.ObjectUtil;
import com.websql.config.SqlParserHandler;
import com.websql.dao.JobLogsRepository;
import com.websql.dao.TimingRepository;
import com.websql.model.JobLogs;
import com.websql.model.Result;
import com.websql.model.SqlParserVo;
import com.websql.model.TimingVo;
import com.websql.service.TimingService;
import com.websql.util.StpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @program: websql
 * @description: 作业服务业务处理
 * @author: rabbit boy_0214@sina.com
 * @create: 2019-09-16 19:28
 **/
@Service
@Slf4j
public class TimingServiceImpl implements TimingService {

    @Resource
    private TimingRepository timingRepository;
    @Resource
    private JobLogsRepository jobLogsRepository;


    @Override
    public TimingVo addTimingData(TimingVo model) throws Exception {
        model.setSqlCreateUser(StpUtils.getCurrentUserName());
        model.setTeamId(StpUtils.getCurrentActiveTeam().getId());
        List<SqlParserVo> list = SqlParserHandler.getParserVo(model.getTimingName(), Base64Decoder.decodeStr(model.getSqlText()));
        for (SqlParserVo sqlParserVo : list) {
            if (sqlParserVo.getMethodType() == null) {
                throw new RuntimeException("sql无法解析执行，请检查修改！");
            }
        }
        return timingRepository.save(model);
    }

    @Override
    public void delTiming(Long id) {
        timingRepository.deleteById(id);
    }


    @Override
    public Result<TimingVo> timingList(TimingVo model) {
        Result<TimingVo> result = new Result<>();
        Long teamId = Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId();
        Specification<TimingVo> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(4);
            if (ObjectUtil.isNotEmpty(model.getTitle())) {
                predicates.add(cb.like(root.get("title"), "%" + model.getTitle() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getTimingName())) {
                predicates.add(cb.like(root.get("timingName"), "%" + model.getTimingName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getSyncName())) {
                predicates.add(cb.like(root.get("syncName"), "%" + model.getSyncName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getState())) {
                predicates.add(cb.like(root.get("state"), "%" + model.getState() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getId())) {
                predicates.add(cb.and(root.get("id").in(model.getId())));
            }
            predicates.add(cb.and(root.get("teamId").in(teamId)));
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<TimingVo> all = timingRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public void updateTiming(TimingVo vo) {
        timingRepository.saveAndFlush(vo);
    }

    @Override
    public void updateStatus(TimingVo vo) {
        timingRepository.saveAndFlush(vo);
    }

    @Override
    public void saveLogs(JobLogs logs) {
        jobLogsRepository.save(logs);
    }

    @Override
    public Result<JobLogs> jobLogsList(JobLogs model) {
        Result<JobLogs> result = new Result<>();
        Long teamId = Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId();
        Specification<JobLogs> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(3);
            if (ObjectUtil.isNotEmpty(model.getTaskName())) {
                predicates.add(cb.like(root.get("taskName"), "%" + model.getTaskName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getExecuteDate())) {
                predicates.add(cb.like(root.get("executeDate"), "%" + model.getExecuteDate() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getTaskState())) {
                predicates.add(cb.like(root.get("taskState"), "%" + model.getTaskState() + "%"));
            }
            predicates.add(cb.and(root.get("teamId").in(teamId)));
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<JobLogs> all = jobLogsRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public void jobLogDelete() {
        jobLogsRepository.deleteAll();
    }

    @Override
    public List<TimingVo> queryTimingJobList(String name) {
        if (ObjectUtil.isNotNull(name)) {
            return timingRepository.queryTimingJobByTitleName(name);
        } else {
            return timingRepository.queryTimingJobList();
        }
    }

    @Override
    public TimingVo queryTimingJobById(Long id) {
        return timingRepository.findById(id).get();
    }
}
