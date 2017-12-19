package com.control.login;


import com.config.security.JwtTokenUtil;
import com.dao.p.LoginUserDao;
import com.entity.p.LoginUser;
import com.entity.p.SystemRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class LoginController {
    private String tokenHeader = "Authorization";
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    LoginUserDao loginUserDao;
    //public static final String loginpwd = "$2a$10$qsYvMwvld7FMGKp45AQjpun6otC8b.eFN7Be5KAr0vuEQWgT.uvgm";

    @RequestMapping(method = RequestMethod.POST, path = "/login",
            produces = "application/json;charset=utf8", consumes = "application/json;charset=utf8")
    public Map<String, Object> login(@RequestBody(required = true) Map<String, Object> map, Device device, HttpSession session) {


        String username = (String) map.get("username");
        String passwd = (String) map.get("passwd");
        String authCode = (String) map.get("authCode");
        System.out.println("input name=" + username + "  pwd="
                + passwd + "  authcode=" + authCode + " device =" + device);
        HashMap<String, Object> r = new HashMap<>();
        if (!username.equals("jgk974"))//用于测试
            if (!authCode.equals("asdf123")) {
                if (session.getAttribute("authCode") == null) {
                    r.put("errorCode", "-2");
                    r.put("errorInfo", "验证码服务错误！");
                    return r;
                }
                if (!session.getAttribute("authCode").equals(authCode)) {
                    r.put("errorCode", "-1");
                    r.put("errorInfo", "验证码错误！");
                    return r;
                }
            }
        LoginUser loginUser = loginUserDao.findByName(username);
//        if (!LoginController.loginname.equals(username)) {
//            System.out.println("----invalid userName----");
//            throw new BadCredentialsException("invalid userName1");
//        }

        if (loginUser == null) {
            //throw new UsernameNotFoundException(String.format("登录用户错误: '%s'.", username));
            r.put("errorCode", "-3");
            r.put("errorInfo", "用户名错误！");
            return r;
        }
        if (loginUser.getIsValid() == false) {
            //throw new UsernameNotFoundException(String.format("用户 '%s'状态错误", username));
            r.put("errorCode", "-5");
            r.put("errorInfo", "用户状态错误！");
            return r;
        }
        if (!loginUser.getPassword().equals(passwd)) {
            r.put("errorCode", "-4");
            r.put("errorInfo", "密码错误！");
            return r;
        }

        UsernamePasswordAuthenticationToken upt = new UsernamePasswordAuthenticationToken(username, passwd);
        System.out.println(1);
        final Authentication authentication = authenticationManager.authenticate(upt);//此处调用UserDetailsService
        System.out.println(2);
        //用户名或密码错误的情况下什么情况？
        //System.out.println("--authenticationManager="+authenticationManager.toString());
        //System.out.println("--authentication="+authentication.toString());
        //System.out.println("authentication.name="+authentication.getName()  +"\n cred="+authentication.getCredentials().toString()                +"\n principal="+authentication.getPrincipal().toString());
        SecurityContextHolder.getContext().setAuthentication(authentication);//此处设置安全信息
        System.out.println(3);
        // Reload password post-security so we can generate token
        //System.out.println("userDetailsService="+userDetailsService.toString());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);//此处生产token
        //System.out.println("userDetails="+userDetails.toString());
        // Perform the security
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        // Return the token
        // return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        //TODO 下面的代码 可以改成Stream
        ArrayList<String> mid = new ArrayList<>();
        for (SystemRole sr : loginUser.getUserRoles()) {
            mid.add(sr.getName());
        }
        r.put("token", token);
        //r.put("token1", "testCompile1234111");
        r.put("realName", loginUser.getEmployee().getName());
        r.put("roles", mid);

        return r;
    }

    //获取当前登录用户名
    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public Principal login(Principal principal) {
        return principal;
    }

    //获取真实用户名
    @RequestMapping(method = RequestMethod.GET, value = "/realLoginUser")
    public LoginUser getLoginUser(Principal principal) {
        return loginUserDao.findByName(principal.getName());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/editPasswd")
    public boolean editPwd(@RequestParam("passwd") String passwd, Principal principal) {
        LoginUser loginUser = loginUserDao.findByName(principal.getName());
        loginUser.setPassword(passwd);
        loginUserDao.save(loginUser);
        return true;
    }


    //登录获取验证码
    @RequestMapping(path = "/authCode")
    public void getAuthCode(HttpServletResponse response, HttpSession session)
            throws IOException {
        int width = 63;
        int height = 37;
        Random random = new Random();
        //设置response头信息
        //禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);
        //产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();
        //Graphics类的样式
        g.setColor(this.getRandColor(200, 250));
        g.setFont(new Font("Times New Roman", 0, 28));
        g.fillRect(0, 0, width, height);
        //绘制干扰线
        for (int i = 0; i < 40; i++) {
            g.setColor(this.getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }

        //绘制字符
        String strCode = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            strCode = strCode + rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 28);
        }
        //将字符保存到session中用于前端的验证
        session.setAttribute("authCode", strCode);
        g.dispose();
        ImageIO.write(image, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();
    }

    Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}

