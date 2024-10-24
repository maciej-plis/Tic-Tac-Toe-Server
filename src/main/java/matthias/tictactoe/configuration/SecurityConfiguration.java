package matthias.tictactoe.configuration;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.jwt.JwtService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .formLogin().disable()
            .addFilterAfter(new BearerTokenAuthenticationFilter(jwtService, userDetailsService), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers(GET, "/").permitAll()
            .antMatchers(POST, "/login-guest", "/register", "/login").anonymous()
            .antMatchers("/games/**").authenticated()
            .antMatchers("/tic-tac-toe/**").permitAll()
            .anyRequest().denyAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }
}

@RequiredArgsConstructor
class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken == null || !bearerToken.startsWith(TOKEN_PREFIX)) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final var parsedToken = jwtService.parseToken(bearerToken.replaceFirst(TOKEN_PREFIX, ""));
            if (parsedToken.isExpired()) {
                final var userDetails = userDetailsService.loadUserByUsername(parsedToken.getSubject());
                final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            response.sendError(SC_FORBIDDEN, ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}

