package blog.admin;


import blog.entity.User;
import blog.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class LoginController {

    @Resource
    UserService userService;

    @GetMapping
    public ModelAndView loginPage(){
        ModelAndView modelAndView = new ModelAndView("/admin/login");
        return modelAndView;
    }
    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession httpSession,
                              RedirectAttributes attributes) throws IOException {
        ModelAndView modelAndView;
        User user = userService.checkUser(username, password);
        if (user!=null) {
            httpSession.setAttribute("user",user);
            user.setPassword("");
            modelAndView = new ModelAndView("/admin/index");
        }else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            modelAndView =new ModelAndView("redirect:/admin");
        }
        return modelAndView;
    }
    /*loginOut*/
    @GetMapping("/loginOut")
    public void loginOut(HttpSession session, HttpServletResponse response) throws IOException {
        session.removeAttribute("user");
        response.sendRedirect("/admin");
    }

}
