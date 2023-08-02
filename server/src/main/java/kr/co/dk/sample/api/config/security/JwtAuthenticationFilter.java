package kr.co.dk.sample.api.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.dk.sample.api.app.auth.dao.AuthDAO;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.model.exception.CustomException;
import kr.co.dk.sample.api.config.security.dto.JwtTokenValidation;
import kr.co.dk.sample.api.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LogManager.getLogger(JwtAuthenticationFilter.class);
    private static final String[] skipFilterUrls = {
            "/api/v1/test/**",
            "/api/v1/openapi/**",
            "/api/v1/auth/**",
            "/api/v1/company/**",
            "/api/v1/site/**",
            "/api/v1/push/**",
            "/api/v1/alarm/**",
            "/api/v1/search/**",
            "/api/v1/labor/**",
            "/api/v1/board/**",
            "/api/v1/job/**",
            "/api/v1/equip/**",
            "/api/v1/like/**",
            "/api/v1/terms/**",
            "/api/v1/chat/**",
            "/api/v1/stomp/**",
            "/api/v1/report/**",
            "/sub/**",
            "/pub/**",
            "/api/v1/upload/**",
            "/api/v1/**/open/**"};
    private static final ObjectMapper mapper = new ObjectMapper();

//    @Autowired
//    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthDAO authDAO;
    @Value("${jwt.user.accessToken.expirationInMinute}")
    private int userAccessTokenExpiration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            Map<String, Object> map = new HashMap<>();
            map.put("token", jwt);
            if (jwt == null || jwt.equals("null")) {
                returnError(response, ErrorCode.CODE_1104);
                return;
            }
            // 복호화 위치
//            jwt = new String(SEED_KISA.DECRYPT(jwt));
            jwt = jwt.trim();

            JwtTokenValidation jwtTokenValidation = tokenProvider.validateToken(jwt);

            if (jwtTokenValidation.isSuccess()) {
//                long userIdx = Long.parseLong(tokenProvider.getUserIdFromJWT(jwt).toString()) ;

                map.put("mb_idx", tokenProvider.getUserIdFromJWT(jwt).toString());
                String agent = request.getHeader("User-Agent");
                map.put("agent_type", StringUtil.getAgentType(agent));

//                // DB세션체크
//                if(jwtTokenValidation.isAccessToken()){
//                    map.put("token_type", "ACCESS");
//                    map.put("expiration_in_minute", userAccessTokenExpiration);
//                }
//                Map<String, Object> dbToken = authDAO.dbValidateToken(map);
//
//                if(dbToken == null){
//                    returnError(response, ErrorCode.CODE_1101);
//                    return;
//                }

                // checkMember을 check 토큰으로
                UserDetails userDetails = authDAO.loadUserByIdx(map);
                if (userDetails != null) {
//                    if (!userDetails.isAccountNonLocked()) {
//                        returnError(response, ErrorCode.CODE_1109);
//                        return;
//                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    returnError(response, ErrorCode.CODE_1106);
                    log.error("user idx does not exist.");
                    return;
                }

            } else if (jwtTokenValidation.isSuccess() == false) {
                returnError(response, jwtTokenValidation.getCode());
                return;
            }

        } catch (CustomException ce) {
            returnError(response, ce.getCode());
            return;
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void returnError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
//        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getWriter(), new ApiResponse(errorCode));
        return;

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(skipFilterUrls).anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
    }


}
