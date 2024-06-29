import jakarta.servlet.http.HttpServletRequest;
import org.example.App;
import org.example.controller.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ContextConfiguration(classes = App.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HttpServletRequest request;

    @Test
    public void testAddUserAsAdmin() throws Exception {
        // Set up the request and expected response
        mockMvc.perform(post("/admin/addUser").contentType(MediaType.APPLICATION_JSON)
                .header("X-Role-Info", "Basic " + Base64.getEncoder().encodeToString("123456:Jim:admin".getBytes()))
                .content("{\"userId\":[\"234567\"],\"endpoint\":[\"resourceA\",\"resourceB\",\"resourceC\"]}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("User access added successfully."));
    }

    @Test
    public void testAddUserAsUser() throws Exception {
        // Set up the request and expected response
        mockMvc.perform(post("/admin/addUser").contentType(MediaType.APPLICATION_JSON)
                        .header("X-Role-Info", "Basic " + Base64.getEncoder().encodeToString("234567:Sam:user".getBytes()))
                        .content("{\"userId\":[\"345678\"],\"endpoint\":[\"resourceD\",\"resourceE\",\"resourceF\"]}")
                )
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access denied. Only admins can add user access."));
    }
}
