package com.config.security;

import com.config.security.JwtAuthenticationTokenFilter;
import com.config.security.JwtRestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 * Created by jianggk on 2016/11/7.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtRestAuthenticationEntryPoint unauthorizedHandler;//提示登录失败
    @Autowired
    private UserDetailsService userDetailsService;//加载用户信息
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {//核心配置1
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService);
        //.passwordEncoder(BcryptPasswordEncoder()); 不用加密密码

    }
    @Bean
    public PasswordEncoder BcryptPasswordEncoder() {
        //$2a$10$qsYvMwvld7FMGKp45AQjpun6otC8b.eFN7Be5KAr0vuEQWgT.uvgm  //对应的密码是111
        return new BCryptPasswordEncoder();
    }
    //    换成Annotation方式以后，则需要使用@EnableGlobalMethodSecurity(prePostEnabled=true)注解来开启。
    //    并且需要提供以下方法：
    //    @Bean
    //    @Override
    //    public AuthenticationManager authenticationManagerBean() throws Exception {
    //        return super.authenticationManagerBean();
    //    }
    //
    //    @Autowired//注意这个方法是注入的
    //    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //        auth.userDetailsService(new JwtUserDetailsServiceImpl());
    //    }
    //!!!至此可以正常拦截

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Autowired
    @Qualifier("primaryDataSource")
    DataSource ds;

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//                        //http.authorizeRequests() .anyRequest().permitAll();
//                //        http.
//                //                httpBasic().and().authorizeRequests()
//                //              //  .antMatchers(        "/api/**","/user")
//                //             //   .permitAll()
//                //                .anyRequest()
//                //                .authenticated()
//                //                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//                //如果是使用的是 Java 代码配置 spring Security，那么 CSRF 保护默认是开启的，那么在 POST 方式提交表单的时候就必须验证 Token，
//                // 如果没有，那么自然也就是 403 没权限了。
//                http.httpBasic()
//                .and()
//                .csrf().disable()
//                .authorizeRequests()
//                        .antMatchers("/users/currentUser","/users/queryPwd/**","/users/valid/**" ).permitAll()
//                        .anyRequest().authenticated()
//                .and().httpBasic().realmName(CustomBasicAuthenticationEntryPoint.REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        //对于Angular2来说，需要设置CSRF token存储，否则浏览器没有办法取得正确的CSRF token，
//        //http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
//    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {//核心配置2
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // allow anonymous resource requests
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/authCode",
                        "/StockPromotion/**",
                        "/wx/**",
                        "/MP_verify_LNKwjvrx0iNDE9om.txt",
                        "/public/**",
                        "/wxfront/**",
                        "/wxf/**"
                ).permitAll()
                .antMatchers(HttpMethod.POST, "/login","/wx/**","/public/**").permitAll()
                //.antMatchers(HttpMethod.POST, "/wx/**").permitAll()
                //.antMatchers(HttpMethod.POST, "/**").permitAll() //debug 时, 不用对 Authorization 作验证
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(jwtAuthenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
        //httpSecurity.logout().deleteCookies();
        //httpSecurity.sessionManagement().maximumSessions(1);
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(ds)
//                .usersByUsernameQuery("select name,password,isvalid from login_user where name=?")
//                .authoritiesByUsernameQuery("select username,auth from (\n" +
//                        "select u.name username,a.name auth\n" +
//                        "from login_user u ,jemtest.j_role r ,jemtest.J_AUTHORITY  a,jemtest.j_user_role ur,jemtest.J_ROLE_AUTH ra\n" +
//                        "where ur.uname=u.name and ur.rname=r.name\n" +
//                        "and  ra.rname=r.name and ra.aname=a.name\n" +
//                        "union \n" +
//                        "select  u.name username,a.name auth  from login_user u,jemtest.J_AUTHORITY a, jemtest.J_USER_AUTH ua\n" +
//                        "where u.name=ua.uname and ua.aname=a.name\n" +
//                        ") where username=?");
//    }

//    @Bean
//    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
//        return new CustomBasicAuthenticationEntryPoint();
//    }

    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

}


//class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
//      public final static  String     REALM = "MY_APP_REALM";
//    @Override
//    public void commence(final HttpServletRequest request,
//                         final HttpServletResponse response,
//                         final AuthenticationException authException) throws IOException, ServletException {
//        //TODO客户端是否弹出自带登录窗口
//                System.out.println("------authException:"+authException.getMessage());
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
//                PrintWriter writer = response.getWriter();
//                writer.println("------HTTP Status 401----------- : " + authException.getMessage());
//    }
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        setRealmName(CustomBasicAuthenticationEntryPoint.REALM);
//        super.afterPropertiesSet();
//    }
//
//}