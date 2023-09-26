package com.example.disinfectplatfrom.session;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * @author qilong.sun
 * @version v1.0
 * @time 2021/10/27 13:15
 * @title
 * @description
 */
public class MyConcurrentSessionFilter extends ConcurrentSessionFilter {
    public MyConcurrentSessionFilter(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
    }

    public MyConcurrentSessionFilter(SessionRegistry sessionRegistry, String expiredUrl) {
        super(sessionRegistry, expiredUrl);
    }

    public MyConcurrentSessionFilter(SessionRegistry sessionRegistry, SessionInformationExpiredStrategy sessionInformationExpiredStrategy) {
        super(sessionRegistry, sessionInformationExpiredStrategy);
    }
}
