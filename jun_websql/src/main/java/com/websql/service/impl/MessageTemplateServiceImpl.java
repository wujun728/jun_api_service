package com.websql.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.websql.dao.SysMessageTemplateRepository;
import com.websql.model.AjaxResult;
import com.websql.model.Result;
import com.websql.model.SysMessageTemplateModel;
import com.websql.service.MessageTemplateService;
import com.websql.util.StpUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.criteria.Predicate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private static final Logger log = LoggerFactory.getLogger(MessageTemplateServiceImpl.class);

    @Autowired
    private SysMessageTemplateRepository repository;

    @Override
    public Result<SysMessageTemplateModel> list(SysMessageTemplateModel model) {
        Result<SysMessageTemplateModel> result = new Result<>();
        Specification<SysMessageTemplateModel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(4);
            if (ObjectUtil.isNotEmpty(model.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + model.getName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getId())) {
                predicates.add(cb.and(root.get("id").in(model.getId())));
            }
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<SysMessageTemplateModel> all = repository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public AjaxResult addMessageTemplate(SysMessageTemplateModel sysMessageTemplateModel) {
        sysMessageTemplateModel.setCreateTime(DateUtil.date());
        sysMessageTemplateModel.setCreateUser(StpUtils.getCurrentUserName());
        SysMessageTemplateModel save = repository.save(sysMessageTemplateModel);
        return AjaxResult.success(save);
    }

    @Override
    public AjaxResult deleteMessageTemplate(Long id) {
        repository.deleteById(id);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult testMessageTemplate(SysMessageTemplateModel model) {
        return AjaxResult.success(sendMessage(model, null));
    }

    public boolean sendMessage(SysMessageTemplateModel model, Map<String, Object> params) {
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("发送告警失败，没有找到告警配置信息!");
        }
        model.setContent(StrUtil.format(model.getContent(), params));
        switch (model.getType()) {
            case "命令行":
                return execCmd(model);
            case "飞书":
                return execFeiShu(model);
            case "钉钉":
                return execDingDing(model);
            default:
                throw new RuntimeException("发送告警失败，不支持的消息类型!");
        }
    }

    /**
     * 发送消息，固定参数： 标题，时间，状态，等级
     *
     * @param id
     * @param params
     */
    @Override
    public boolean sendMessage(Long id, Map<String, Object> params) {
        SysMessageTemplateModel model = repository.getConfigById(id);
        return sendMessage(model, params);
    }

    private boolean execCmd(SysMessageTemplateModel model) {
        try {
            String result = RuntimeUtil.execForStr(model.getContent());
            log.info("exec cmd result:{}", result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean execFeiShu(SysMessageTemplateModel model) {
        try {
            long timestamp = System.currentTimeMillis() / 1000;
            String stringToSign = timestamp + "\n" + model.getSecret();
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(new byte[]{});
            String token = new String(Base64.encodeBase64(signData));
            Map<String, Object> param = new HashMap<>(1);
            param.put("msg_type", "text");
            param.put("sign", token);
            param.put("timestamp", timestamp);
            Map<String, String> content = new HashMap<>(1);
            content.put("text", model.getContent());
            param.put("content", content);
            HttpResponse execute = HttpUtil.createPost(model.getUrl()).contentType("application/json; charset=utf-8").body(JSON.toJSONString(param)).execute();
            JSONObject jsonObject = JSON.parseObject(execute.body());
            if (!ObjectUtil.equal(jsonObject.getInteger("code"), 0)) {
                log.error("发送飞书消息返回失败,{}", JSON.toJSONString(jsonObject));
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送飞书消息失败!,{}", e.getMessage());
        }
        return false;
    }


    private boolean execDingDing(SysMessageTemplateModel model) {
        try {
            Long timestamp = System.currentTimeMillis();
            String secret = model.getSecret();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            Map<String, Object> param = new HashMap<>(1);
            param.put("msgtype", "text");
            param.put("timestamp", timestamp);
            Map<String, String> content = new HashMap<>(1);
            content.put("content", model.getContent());
            param.put("text", content);
            String url = model.getUrl() + "&sign=" + sign + "&timestamp=" + timestamp;
            HttpResponse execute = HttpUtil.createPost(url).contentType("application/json; charset=utf-8").body(JSON.toJSONString(param)).execute();
            JSONObject jsonObject = JSON.parseObject(execute.body());
            if (!ObjectUtil.equal(jsonObject.getInteger("errcode"), 0)) {
                log.error("发送钉钉消息返回失败,{}", JSON.toJSONString(jsonObject));
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public List<Map<String, String>> findMessageTemplateList() {
        List<SysMessageTemplateModel> list = repository.findAll(Sort.by("id").descending());
        List<Map<String, String>> resultList = new ArrayList<>(list.size());
        for (SysMessageTemplateModel model : list) {
            Map<String, String> item = new HashMap<>(3);
            item.put("code", model.getId().toString());
            item.put("value", model.getName());
            item.put("id", model.getId().toString());
            resultList.add(item);
        }
        return resultList;
    }

    @Override
    public SysMessageTemplateModel queryMessageTemplateById(Long id) {
        return repository.findById(id).orElse(new SysMessageTemplateModel());
    }
}
