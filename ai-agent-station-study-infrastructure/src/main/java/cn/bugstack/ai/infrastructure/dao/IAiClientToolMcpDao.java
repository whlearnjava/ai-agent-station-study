package cn.bugstack.ai.infrastructure.dao;

import cn.bugstack.ai.infrastructure.dao.po.AiClientToolMcp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * MCP客户端配置表 DAO

 */
@Mapper
public interface IAiClientToolMcpDao {

    /**
     * 插入MCP客户端配置
     */
    int insert(AiClientToolMcp aiClientToolMcp);

    /**
     * 根据ID更新MCP客户端配置
     */
    int updateById(AiClientToolMcp aiClientToolMcp);

    /**
     * 根据MCP ID更新MCP客户端配置
     */
    int updateByMcpId(AiClientToolMcp aiClientToolMcp);

    /**
     * 根据ID删除MCP客户端配置
     */
    int deleteById(Long id);

    /**
     * 根据MCP ID删除MCP客户端配置
     */
    int deleteByMcpId(String mcpId);

    /**
     * 根据ID查询MCP客户端配置
     */
    AiClientToolMcp queryById(Long id);

    /**
     * 根据MCP ID查询MCP客户端配置
     */
    AiClientToolMcp queryByMcpId(String mcpId);

    /**
     * 查询所有MCP客户端配置
     */
    List<AiClientToolMcp> queryAll();

    /**
     * 根据状态查询MCP客户端配置
     */
    List<AiClientToolMcp> queryByStatus(Integer status);

    /**
     * 根据传输类型查询MCP客户端配置
     */
    List<AiClientToolMcp> queryByTransportType(String transportType);

    /**
     * 查询启用的MCP客户端配置
     */
    List<AiClientToolMcp> queryEnabledMcps();

}