package kr.co.dk.sample.api.config.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Aspect
@Component
public class ControllerLogger {

    ObjectMapper objectMapper = new ObjectMapper();

    private static JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }

    @Pointcut("execution(* kr.co.dk.sample.api.app.*.controller..*Controller.*(..))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object doLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String transactionId = UUID.randomUUID().toString().substring(0, 7);
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        LinkedList<String> controllerNames = new LinkedList<>(Arrays.asList(joinPoint.getSignature().getDeclaringType().getName().split("\\.")));
        String controllerName = controllerNames.getLast();
        String methodName = joinPoint.getSignature().getName();
        String requestUri = request.getRequestURI();
        String httpMethod = request.getMethod();
        String token = request.getHeader("authorization");

        if (token == null || token.trim().equals("Bearer")) {
            token = "null";
        } else {
            token = token.replace("Bearer ", "");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("controller", controllerName);
        params.put("request_uri", requestUri);
        params.put("method", methodName);
        params.put("http_method", httpMethod);
        params.put("params", getParams(request));
        log.info("==API_START== [TRANSACTION_ID] : " + transactionId + " [TOKEN] : " + token + " [REQ] : {}", params);


        Object after = joinPoint.proceed();

        Map response = objectMapper.convertValue(after, Map.class);
        response.put("controller", controllerName);
        response.put("request_uri", requestUri);
        response.put("method", methodName);
        response.put("http_method", httpMethod);
        long endTime = System.currentTimeMillis();
        log.info("===API_END=== [TRANSACTION_ID] : " + transactionId + " [TOKEN] : " + token + " [RES] : {} [ELAPSED_TIME] : " + (endTime - startTime) + "ms", response);

        return after;
    }
}
