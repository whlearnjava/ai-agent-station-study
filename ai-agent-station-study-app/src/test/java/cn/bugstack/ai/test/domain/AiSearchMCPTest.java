package cn.bugstack.ai.test.domain;

import com.alibaba.fastjson.JSON;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

/**
 * 百度搜索的使用Test
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AiSearchMCPTest {
    @Test
    public void test() {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("https://juziapi.xin/")
                        .apiKey("sk-dM4fqhydVpnBv56vN1jU9cMiqtjUFGedVI8o9064BvpGF1IP")
                        .completionsPath("v1/chat/completions")
                        .embeddingsPath("v1/embeddings")
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1-mini")
                        .toolCallbacks(new SyncMcpToolCallbackProvider(seeMcpClient()).getToolCallbacks())
                        .build())
                .build();

        ChatResponse call = chatModel.call(Prompt.builder()
                .messages(new UserMessage("搜索《逆天邪神》的作者是谁"))
                .build());

        log.info("测试结果:{}", JSON.toJSONString(call.getResult()));

    }

    public McpSyncClient seeMcpClient(){
        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder("http://appbuilder.baidu.com/v2/ai_search/mcp/")
                .sseEndpoint("sse?api_key=bce-v3/ALTAK-nX3hWOIBMplWfDSxKWakV/e1aa0c4d7c0503a5ceb345370c8c64b21d483ec4")
                .build();

        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(360)).build();

        var init_sse = mcpSyncClient.initialize();
        log.info("SSE:{}",init_sse);

        return mcpSyncClient;
    }

}
