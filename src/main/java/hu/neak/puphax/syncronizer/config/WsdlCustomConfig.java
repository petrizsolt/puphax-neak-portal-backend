package hu.neak.puphax.syncronizer.config;


import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;


@EnableWs
@Configuration
public class WsdlCustomConfig {
	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
	    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
	    servlet.setApplicationContext(applicationContext);
	    return new ServletRegistrationBean<>(servlet, "/neak/*");
	}
	
	@Bean(name = "puphax")
	public Wsdl11Definition puphaWsdl11Definition() {
	    return wsdlDefinition("puphax", "PUPHAXWS.wsdl");
	}
	
	private Wsdl11Definition wsdlDefinition(String... path) {
	    SimpleWsdl11Definition def = new SimpleWsdl11Definition();
	    def.setWsdl(new ClassPathResource("PUPHAXWS.wsdl"));
	    return def;
	}
	

}
