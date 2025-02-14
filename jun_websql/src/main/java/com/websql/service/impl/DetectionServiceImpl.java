package com.websql.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.websql.config.SqlParserHandler;
import com.websql.dao.DetectionLogsRepository;
import com.websql.dao.DetectionRepository;
import com.websql.model.Result;
import com.websql.model.SqlParserVo;
import com.websql.model.SysDetectionLogsModel;
import com.websql.model.SysDetectionModel;
import com.websql.service.DetectionService;
import com.websql.service.MessageTemplateService;
import com.websql.util.StpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DetectionServiceImpl implements DetectionService {

    private static final Logger log = LoggerFactory.getLogger(DetectionServiceImpl.class);

    @Autowired
    private DetectionRepository detectionRepository;

    @Autowired
    private DetectionLogsRepository detectionLogsRepository;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Override
    public Result<SysDetectionModel> list(SysDetectionModel model) {
        Result<SysDetectionModel> result = new Result<>();
        Long teamId = Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId();
        Specification<SysDetectionModel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(4);
            if (ObjectUtil.isNotEmpty(model.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + model.getName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getDataBaseName())) {
                predicates.add(cb.like(root.get("dataBaseName"), "%" + model.getDataBaseName() + "%"));
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
        Page<SysDetectionModel> all = detectionRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        for (SysDetectionModel sysDetectionModel : all.getContent()) {
            if (ObjectUtil.isNotNull(sysDetectionModel.getMessageId())) {
                sysDetectionModel.setMessageName(messageTemplateService.queryMessageTemplateById(sysDetectionModel.getMessageId()).getName());
            }
        }
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public SysDetectionModel add(SysDetectionModel model) {
        model.setCreateTime(DateUtil.date());
        model.setCreateUser(StpUtils.getCurrentUserName());
        model.setTeamId(StpUtils.getCurrentActiveTeam().getId());
        String sql = Base64Decoder.decodeStr(model.getSqlContent());
        List<SqlParserVo> parserVoList = SqlParserHandler.getParserVo(model.getDataBaseName(), sql);
        if (parserVoList.size() != 1) {
            throw new RuntimeException("仅限监测单条查询SQL，请拆分SQL后添加!");
        }
        for (SqlParserVo sqlParserVo : parserVoList) {
            if (ObjectUtil.notEqual(sqlParserVo.getMethodType(), SqlParserHandler.SELECT)) {
                throw new RuntimeException("SQL非查询语句类不允许执行!");
            }
        }
        return detectionRepository.save(model);
    }

    @Override
    public void deleteById(Long id) {
        detectionRepository.deleteById(id);
    }

    @Override
    public void updateById(SysDetectionModel vo) {
        detectionRepository.saveAndFlush(vo);
    }

    @Override
    public SysDetectionModel selectById(Long id) {
        return detectionRepository.selectById(id);
    }

    @Override
    public List<Map<String, String>> selectAllByActiveId(String id) {
        SysDetectionModel model = new SysDetectionModel();
        model.setTeamId(Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId());
        List<SysDetectionModel> list = detectionRepository.findAll(Example.of(model), Sort.by("id").descending());
        List<Map<String, String>> resultList = new ArrayList<>(list.size());
        for (SysDetectionModel sysDetectionModel : list) {
            Map<String, String> item = new HashMap<>(3);
            item.put("code", sysDetectionModel.getId().toString());
            item.put("value", sysDetectionModel.getName());
            item.put("id", sysDetectionModel.getId().toString());
            item.put("select", ObjectUtil.equal(id, sysDetectionModel.getId().toString()) ? "true" : "false");
            resultList.add(item);
        }
        return resultList;
    }

    @Override
    public Result<SysDetectionLogsModel> logList(SysDetectionLogsModel model) {
        Result<SysDetectionLogsModel> result = new Result<>();
        Long teamId = Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId();
        Specification<SysDetectionLogsModel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(4);
            if (ObjectUtil.isNotEmpty(model.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + model.getName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getDataBaseName())) {
                predicates.add(cb.like(root.get("dataBaseName"), "%" + model.getDataBaseName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getId())) {
                predicates.add(cb.and(root.get("id").in(model.getId())));
            }
            if (ObjectUtil.isNotEmpty(model.getTaskId())) {
                predicates.add(cb.and(root.get("taskId").in(model.getTaskId())));
            }
            if (ObjectUtil.isNotEmpty(model.getBeginDate())) {
                predicates.add(cb.between(root.get("createTime"), DateUtil.parseDateTime(model.getBeginDate()), DateUtil.parseDateTime(model.getEndDate())));
            }
            predicates.add(cb.and(root.get("teamId").in(teamId)));
            query.orderBy(cb.desc(root.get("createTime")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<SysDetectionLogsModel> all = detectionLogsRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public Map<String, Object> logCharts(SysDetectionLogsModel model) {
        model.setPage(1);
        model.setLimit(Integer.MAX_VALUE);
        Result<SysDetectionLogsModel> sysDetectionLogsModelResult = logList(model);
        List<SysDetectionLogsModel> list = sysDetectionLogsModelResult.getList();
        Map<String, Object> resultMap = new HashMap<>(2);
        Set<String> dateList = new TreeSet<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, List<SysDetectionLogsModel>> listMap = list.stream().collect(Collectors.groupingBy(SysDetectionLogsModel::getName));
        listMap.forEach((k, v) -> {
            List<Object> itemList = new ArrayList<>();
            for (SysDetectionLogsModel sysDetectionModel : v) {
                String date = DateUtil.format(sysDetectionModel.getCreateTime(), "MMdd HH:mm:ss");
                dateList.add(date);
                List<Object> item2 = new ArrayList<>();
                item2.add(date);
                item2.add(sysDetectionModel.getData() == null ? 0 : sysDetectionModel.getData());
                itemList.add(item2);
            }
            HashMap<String, Object> label = new HashMap<>(1);
            label.put("show", true);
            Map<String, Object> itemMap = new HashMap<>(2);
            itemMap.put("name", k);
            itemMap.put("type", "line");
            itemMap.put("connectNulls", true);
            itemMap.put("stack", "a");
            itemMap.put("smooth", true);
            itemMap.put("symbol", "none");
            itemMap.put("label", label);
            itemMap.put("areaStyle", "{}");
            itemMap.put("data", itemList);
            dataList.add(itemMap);
        });
        resultMap.put("dateList", dateList);
        resultMap.put("dataList", dataList);
        return resultMap;
    }

    @Override
    public void deleteLog(Long id) {
        detectionLogsRepository.deleteById(id);
    }

    @Override
    public void deleteLogAll() {
        detectionLogsRepository.deleteAll();
    }
}
