package com.config.security.test;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//        Spring Security中的验证authentication 到底是什么？
//        让我们考虑一个每个人都熟悉的标准验证场景：
//        1、一个用户被提示使用用户名和密码登录
//        2、系统成功的验证了用户名与密码是匹配的
//        3、获取到用户的上下文信息(角色列表等)
//        4、建立这个用户的安全上下文(security context )
//        5、用户可能继续进行一些受到访问控制机制保护的操作，访问控制机制会依据当前安全上下文信息检查这个操作所需的权限。
//
//        前三条组成了验证过程，因此我们要看一下在Spring Security中这是如何发生的：
//        1、用户名和密码被获取到，并放入一个 UsernamePasswordAuthenticationToken 实例中( Authentication接口的一个实例，我们之前已经看到过)。
//        2、这个token被传递到一个 AuthenticationManager 实例中进行验证
//        3、在成功验证后， AuthenticationManager返回一个所有字段都被赋值的 Authentication 对象实例
//        4、通过调用 SecurityContextHolder.getContext().setAuthentication(…)创建安全上下文，通过返回的验证对象进行传递。


public class AuthenticationExample {
    private static AuthenticationManager authenticationManager = new SampleAuthenticationManager();

    public static void main1(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Please enter your username:");
            String name = in.readLine();
            System.out.println("Please enter your password:");
            String password = in.readLine();
            try {
                Authentication request = new UsernamePasswordAuthenticationToken(name, password);
                System.out.println("before:" + request);
                Authentication result = authenticationManager.authenticate(request);
                System.out.println("after:" + result);
//                事实上，Spring Security并不关心你如何将 Authentication 对象放入 SecurityContextHolder中。
//                唯一的关键要求是在 AbstractSecurityInterceptor 验证一个用户请求之前确保 SecurityContextHolder
//                包含一个用于表示principal的 Authentication 对象。

//                你可以(许多用户都这样做)编写自己的Filter或者MVC controller，来提供与那些不是基于Spring Security的验证系统的互操作能力。
//                例如，你可能会使用容器管理的验证机制，通过ThreadLocal或者JNDI地址来使当前用户可用。或者你可能在为一个有着遗留验证系统的公司工作。
//                在这类场景下，很容易可以让Spring Security工作，并且仍然提供验证能力。
//                所有你需要做的是编写一个过滤器，从某个位置读取第三方用户信息，构建一个特定的Spring Security Authentication 对象，
// 并将其放入 SecurityContextHolder中。在这种情况下，你需要考虑在内置的验证基础结构上自动应用这些。
//                例如，你可能需要在返回给客户端响应之前，预先创建一个Http Session对象来为不同线程缓存安全上下文
                SecurityContextHolder.getContext().setAuthentication(result);
                break;
            } catch (AuthenticationException e) {
                System.out.println("Authentication failed: " + e.getMessage());
            }
        }
        System.out.println("Successfully authenticated. Security context contains: " +
                SecurityContextHolder.getContext().getAuthentication());
    }
}

class SampleAuthenticationManager implements AuthenticationManager {
    private static final List<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

@Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (auth.getName().equals(auth.getCredentials())) {
            return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), AUTHORITIES);
        }
        throw new BadCredentialsException("Bad Credentials");
    }
}


//考虑一个传统的web应用中的验证过程：
//        1、你访问主页，并且点击一个链接
//        2、一个请求被发送给服务器，服务器判断你是否在请求一个受保护的资源
//        3、如果你当前没有经过验证，服务器返回一个响应表明你必须要进行验证。响应可以是通过HTTP响应码或者直接重定向到一个特定的网页。
//        4、根据验证机制，你的浏览器可能会重定向到一个特定的网页以至于你可以填写表单，或者浏览器会检索你的身份(通过一个基础验证对话框，一个cookie或者X.509证书，等等)。
//        5、浏览器给服务器回复一个响应。这可能是一个包含你填充好的表单内容的HTTP POST请求，或者一个包含你的验证信息的HTTP请求头。
//        6、下一步服务器会判断当前的验证信息是否是正确的。如果是，可以继续下一步。如果不是，通常你的浏览器会被要求重试(因此你又回到了上两步)。
//        7、你的原始的验证过程的请求将会被重试。希望你验证后能被赋予足够的权限来访问受保护的资源。如果是，请求将会成功，否则，你将会获得一个403 HTTP响应码，表示"禁止"。
//
//        对于以上提到的大部分步骤，Spring Security都有不同的类来负责。
//        主要的参与者(按照使用的顺序)是 ExceptionTranslationFilter， AuthenticationEntryPoint 和验证机制，负责调用我们之前提到的 AuthenticationManager。

//        ExceptionTranslationFilter是一个Spring Security的过滤器，负责检测任何Spring Security抛出的异常。这些异常通常是通过一个 AbstractSecurityInterceptor抛出，这是验证服务的一个主要提供者。我们将会在下一节讨论 AbstractSecurityInterceptor ，但是现在我们仅仅需要知道其是用于产生Java异常，并不知道HTTP或者如何验证一个principal。
//
//        取而代之的是 ExceptionTranslationFilter 来提供这个服务，负责返回403错误码(如果principal已经被验证并且缺乏足够的访问权限-上面的第七步)
//        或者
//        启动一个 AuthenticationEntryPoint (如果principal没有被验证，我们需要进行上面的第三步)。
//
//        AuthenticationEntryPoint