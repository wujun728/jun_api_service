package com.websql.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.websql.dao.*;
import com.websql.model.*;
import com.websql.service.TeamSourceService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeamSourceServiceImpl implements TeamSourceService {


    @Resource
    private TeamResourceRepository teamResourceRepository;


    @Resource
    private TeamSourceRepository teamSourceRepository;


    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private DbSourceRepository dbSourceRepository;

    @Resource
    private DbSqlTextRepository dbSqlTextRepository;

    @Resource
    private TimingRepository timingRepository;

    /**
     * 修改团队所属资源
     *
     * @param teams       团队IDS
     * @param resourceIds 资源IDS
     * @param type        资源类型
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTeamResources(List<String> teams, List<Long> resourceIds, String type) {
        if (!teams.isEmpty()) {
            if (!resourceIds.isEmpty()) {
                teamResourceRepository.deleteResourceByResId(resourceIds, type);
            }
            for (Long resourceId : resourceIds) {
                for (String id : teams) {
                    TeamResourceModel teamResourceModel = new TeamResourceModel();
                    teamResourceModel.setResourceId(resourceId);
                    teamResourceModel.setTeamId(Long.valueOf(id));
                    teamResourceModel.setResourceType(type);
                    teamResourceModel.setCreateTime(DateUtil.date());
                    teamResourceRepository.save(teamResourceModel);
                }
            }
        }
    }


    @Override
    public List<Map<String, String>> queryTeamAllBySelect() {
        TeamSourceModel param = new TeamSourceModel();
        param.setState(0);
        List<TeamSourceModel> teamList = teamSourceRepository.findAll(Example.of(param));
        List<Map<String, String>> resultList = new ArrayList<>(teamList.size());
        for (TeamSourceModel teamSourceModel : teamList) {
            Map<String, String> item = new HashMap<>(4);
            item.put("code", teamSourceModel.getId().toString());
            item.put("value", teamSourceModel.getTeamName());
            item.put("select", "false");
            resultList.add(item);
        }
        return resultList;
    }


    @Override
    public List<TeamResourceModel> queryTeamResourceById(List<Long> ids, String type) {
        if (ObjectUtil.isNull(ids) || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return teamResourceRepository.queryTeamResourceById(ids, type);
    }

    @Override
    public List<TeamResourceModel> queryTeamResourceByTeamId(List<Long> ids, String type) {
        return teamResourceRepository.queryTeamResourceByTeamId(ids, type);
    }

    @Override
    public AjaxResult deleteTeam(Long id) {
        Result<Map<String, Object>> mapResult = queryTeamResourceList(id);
        if (mapResult.getCount() > 0) {
            return AjaxResult.error("存在已授权资源,不允许删除!");
        }
        teamSourceRepository.deleteById(id);
        return AjaxResult.success();
    }


    @Override
    public AjaxResult addTeamSource(TeamSourceModel teamSourceModel) {
        TeamSourceModel param = new TeamSourceModel();
        param.setTeamName(teamSourceModel.getTeamName());
        if (teamSourceRepository.count(Example.of(param)) > 0) {
            return AjaxResult.error("团队名称已经存在,换个名字!");
        }
        teamSourceModel.setCreateTime(DateUtil.date());
        teamSourceRepository.save(teamSourceModel);
        return AjaxResult.success("新增成功!");
    }

    @Override
    public List<TeamSourceModel> queryValidTeamList() {
        TeamSourceModel param = new TeamSourceModel();
        param.setState(0);
        return teamSourceRepository.findAll(Example.of(param));
    }

    @Override
    public List<TeamSourceModel> queryTeamByIds(List<Long> ids) {
        List<TeamSourceModel> list = teamSourceRepository.findAllById(ids);
        return list.stream().filter(s -> s.getState().equals(0)).collect(Collectors.toList());
    }

    @Override
    public Result<TeamSourceModel> selectTeamList(TeamSourceModel model) {
        Result<TeamSourceModel> result = new Result<>();
        Specification<TeamSourceModel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(1);
            if (ObjectUtil.isNotEmpty(model.getTeamName())) {
                predicates.add(cb.like(root.get("teamName"), "%" + model.getTeamName() + "%"));
            }
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<TeamSourceModel> all = teamSourceRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        if (!all.getContent().isEmpty()) {
            List<SysUser> userList = sysUserRepository.findAllById(all.getContent().stream().map(TeamSourceModel::getUserId).filter(ObjectUtil::isNotNull).collect(Collectors.toSet()));
            Map<Long, String> userMap = userList.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getName));
            all.getContent().parallelStream().forEach(s -> {
                s.setStateName(s.getState() == 0 ? "启用" : "禁用");
                s.setUserName(userMap.get(s.getUserId()) == null ? "" : userMap.get(s.getUserId()));
            });
        }
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public Result<Map<String, Object>> queryTeamResourceList(Long id) {
        TeamResourceModel param = new TeamResourceModel();
        param.setTeamId(id);
        List<TeamResourceModel> list = teamResourceRepository.findAll(Example.of(param));
        //团队
        TeamSourceModel teamSourceModel = teamSourceRepository.findById(id).get();
        //人员
        List<SysUser> userList = sysUserRepository.findAllById(list.stream().filter(s -> "USER".equals(s.getResourceType())).map(TeamResourceModel::getResourceId).collect(Collectors.toList()));
        Map<Long, SysUser> userMap = userList.stream().collect(Collectors.toMap(SysUser::getUserId, s -> s));
        //数据源
        List<DataSourceModel> dataSourceModels = dbSourceRepository.findAllById(list.stream().filter(s -> "DATASOURCE".equals(s.getResourceType())).map(TeamResourceModel::getResourceId).collect(Collectors.toList()));
        Map<Long, DataSourceModel> dataMap = dataSourceModels.stream().collect(Collectors.toMap(DataSourceModel::getId, s -> s));
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (TeamResourceModel teamResourceModel : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("teamId", teamResourceModel.getTeamId());
            item.put("teamName", teamSourceModel.getTeamName());
            item.put("datetime", DateUtil.formatDateTime(teamResourceModel.getCreateTime()));
            if ("USER".equals(teamResourceModel.getResourceType())) {
                SysUser sysUser = userMap.get(teamResourceModel.getResourceId());
                item.put("resourceId", sysUser == null ? "" : sysUser.getUserId());
                item.put("resourceName", sysUser == null ? "" : "登录账号:" + sysUser.getUserName());
                item.put("resourceType", "登录用户");
            }
            if ("DATASOURCE".equals(teamResourceModel.getResourceType())) {
                DataSourceModel data = dataMap.get(teamResourceModel.getResourceId());
                item.put("resourceId", data == null ? "" : data.getId());
                item.put("resourceName", data == null ? "" : data.getDbName());
                item.put("resourceType", "数据源");
            }
            resultList.add(item);
        }
        //sql文本
        List<DbSqlText> sqlTextList = dbSqlTextRepository.queryListByTeamId(id);
        for (DbSqlText sqlText : sqlTextList) {
            Map<String, Object> item = new HashMap<>();
            item.put("teamId", id);
            item.put("teamName", teamSourceModel.getTeamName());
            item.put("resourceId", sqlText.getId());
            item.put("resourceName", sqlText.getTitle());
            item.put("resourceType", "SQL文本");
            item.put("datetime", sqlText.getSqlCreateDate());
            resultList.add(item);
        }
        //作业任务
        List<TimingVo> timingVoList = timingRepository.queryListByTeamId(id);
        for (TimingVo timingVo : timingVoList) {
            Map<String, Object> item = new HashMap<>();
            item.put("teamId", id);
            item.put("teamName", teamSourceModel.getTeamName());
            item.put("resourceId", timingVo.getId());
            item.put("resourceName", timingVo.getTitle());
            item.put("resourceType", "作业任务");
            item.put("datetime", timingVo.getSqlCreateDate());
            resultList.add(item);
        }
        Result<Map<String, Object>> result = new Result<>();
        result.setList(resultList);
        result.setCount(resultList.size());
        return result;
    }


    @Override
    public void deleteResourceByResIds(List<Long> ids, String type) {
        teamResourceRepository.deleteResourceByResId(ids, type);
    }

    @Override
    public List<TeamSourceModel> selectTeamListAll() {
        return teamSourceRepository.findAll();
    }
}
