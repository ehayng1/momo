package kr.co.dk.sample.api.app.user.controller;

import kr.co.dk.sample.api.config.security.JwtAuthenticationEntryPoint;
import kr.co.dk.sample.api.config.security.SecurityConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtAuthenticationEntryPoint.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mock;

    //    @Test
    public void profileTest() throws Exception {
        mock.perform(get("/user/profile")).andExpect(content().string("유저 정보임"));
    }
}
