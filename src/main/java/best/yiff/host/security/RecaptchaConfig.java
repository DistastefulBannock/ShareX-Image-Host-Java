/**
 * 
 */
package best.yiff.host.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import de.triology.recaptchav2java.ReCaptcha;

/**
 * @author DistastefulBannock
 *
 */
@Configuration
public class RecaptchaConfig {
	
	@Value(value = "${recaptcha.secret}")
	private String recaptchaSecret;
	
	@Bean
	public ReCaptcha reCaptcha() {
		return new ReCaptcha(recaptchaSecret);
	}
	
	@Bean
	public RecaptchaAuthFilter recaptchaAuthFilter() {
		return new RecaptchaAuthFilter();
	}
	
	@Bean
	public FilterRegistrationBean<RecaptchaAuthFilter> recaptchaFilterRegistration(RecaptchaAuthFilter recaptchaAuthFilter) {
        FilterRegistrationBean<RecaptchaAuthFilter> filterRegistrationBean = new FilterRegistrationBean<RecaptchaAuthFilter>();
        filterRegistrationBean.addUrlPatterns("/plogin");
        filterRegistrationBean.addUrlPatterns("/pregister");
        filterRegistrationBean.setFilter(recaptchaAuthFilter);
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
	}
	
}
