package com.websql.service;


import com.websql.model.AjaxResult;
import com.websql.model.Result;
import com.websql.model.TeamResourceModel;
import com.websql.model.TeamSourceModel;

import java.util.List;
import java.util.Map;

/**
 * @ClassName : TeamSourceService
 * @Description : 团队管理
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2024/03/23 20:01
 */
public interface TeamSourceService {

    void updateTeamResources(List<String> teams, List<Long> resourceIds, String type);

    List<Map<String, String>> queryTeamAllBySelect();

    List<TeamResourceModel> queryTeamResourceById(List<Long> resourceIds, String type);

    List<TeamResourceModel> queryTeamResourceByTeamId(List<Long> teamIds, String type);

    AjaxResult deleteTeam(Long id);

    AjaxResult addTeamSource(TeamSourceModel teamSourceModel);

    List<TeamSourceModel> queryValidTeamList();

    List<TeamSourceModel> queryTeamByIds(List<Long> collect);

    Result<TeamSourceModel> selectTeamList(TeamSourceModel teamSourceModel);

    Result<Map<String, Object>> queryTeamResourceList(Long id);

    void deleteResourceByResIds(List<Long> ids, String type);

    List<TeamSourceModel> selectTeamListAll();

}
