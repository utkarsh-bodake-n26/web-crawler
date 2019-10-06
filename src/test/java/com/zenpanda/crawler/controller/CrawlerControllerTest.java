package com.zenpanda.crawler.controller;

import com.zenpanda.crawler.service.CrawlerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CrawlerController.class)
public class CrawlerControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private CrawlerService crawlerService;

    @Test
    void shouldReturn400AsWithNoQueryParams() throws Exception {

        mockMvc.perform(get(CrawlerController.REQUEST_PATH_API_CRAWLER)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn200() throws Exception {
        String startUrl = "https://google.com";

        MvcResult mvcResult = mockMvc.perform(get(CrawlerController.REQUEST_PATH_API_CRAWLER + "?url=" + startUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEmpty();
    }
}
